# Navigation

A **type-safe, scalable, testable, and production-proven** navigation system built for modern
Android apps using Jetpack Compose + Navigation + Hilt + Kotlin Serialization.

## Core Principles

The navigation system is designed around a set of core principles that prioritize type-safety and scalability.

| Principle | Implementation & Benefit |
|--------------------------------|-----------------------------------------------------------------------------------------------------|
| **Reusable UI**                | No `NavController` in UI → UI components stay dumb and reusable                                     |
| **Type-safe**                  | Sealed interfaces + `@Serializable` + `hasRoute<T>()` → no strings, no crashes                      |
| **flows through `NavManager`** | All navigation goes through `NavManager` No other class talks to `NavController` or reads backstack |
| **Modular & scalable**         | Separate graph functions per feature → easy to maintain, test, and extend                           |
| **Deep link safe**             | Full support out of the box - no extra code needed                                                  |
| **Testable**                   | `NavManager` is Hilt-injectable singleton → easy to mock in unit & UI tests                         |

## Key Components

The following components work together to provide a cohesive and type-safe navigation experience.

| Component | Responsibility                                                                             |
|--------------------|--------------------------------------------------------------------------------------------|
| 1. `NavTarget`     | Sealed hierarchy of **all app destinations** - the heart of type-safe navigation           |
| 2. `NavItem`       | Visual + behavioral representation of a navigation item (icon, title, badge, role)         |
| 3. `NavRole`       | Defines where the item appears: bottom tab, top navigation, action button, etc.            |
| 4. `NavAction`     | Sealed class representing navigation commands (`To`, `Up`, `ResetTo`, etc.)                |
| 5. `NavManager`    | Singleton brain: emits commands, exposes current destination, fully injectable             |
| 6. `*.NavGraph.kt` | Feature-isolated graph builders (`authNavGraph`, `bottomNavGraph`, `alertsNavGraph`, etc.) |
| 7. `NavApp`        | Root composable - the **only** place that talks to `NavController`                         |
| 8. UI Components   | `NavBarRoot`, `NavBarTop`, `NavBarIcon`, ... - pure UI, zero navigation logic              |

### NavTarget

The `NavTarget` sealed interface defines all possible destinations within the application.

- The **foundation** of the entire system.
- No string routes. No `::class.qualifiedName`. No reflection.
- Uses Jetpack Navigation’s `hasRoute<T>()` → **100% compile-safe and R8-safe**.

```kotlin
@Serializable
sealed interface NavTarget {
    companion object {
        fun NavBackStackEntry?.fromBackStackEntry(): NavTarget? {
            return when {
                this?.destination?.hasRoute<NavTargetTop.Settings>() == true   -> NavTargetTop.Settings
                this?.destination?.hasRoute<NavTargetTop.Previous>() == true   -> NavTargetTop.Previous

                this?.destination?.hasRoute<NavTargetRoot.Shabbat>() == true   -> NavTargetRoot.Shabbat
                else                                                           -> null
            }
        }
    }
}

@Serializable
sealed interface NavTargetTop : NavTarget {
    @Serializable object Previous : NavTargetTop
    @Serializable object Settings : NavTargetBottom
}
```

### NavItem

The `NavItem` data class represents the visual and behavioral properties of a navigation element.
Encapsulates everything needed to render a navigation item in bottom bar, or top bar.

```kotlin
data class NavItem(
    val target: NavTarget,
    val title: String?,
    val selectedIcon: UiIcon,
    val unselectedIcon: UiIcon,
    val role: NavRole,
)

object NavItems {

    val Settings = NavItem(
        target = NavTargetTop.Settings,
        title = UiText.Resource(R.string.nav_settings),
        selectedIcon = UiIcon.Resource(R.drawable.settings_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.settings_outlined_24),
        role = NavRole.TOP_ACTION,
    )
    // ...
}
```

### NavRole

The `NavRole` enum defines the placement and specific behavior of a navigation item within the UI.

```kotlin
enum class NavRole {
    ROOT,
    TOP_NAVIGATION,
    TOP_ACTION,
    // ...
}
```

### NavAction

- Sealed hierarchy of all possible navigation actions.
- Emitted by NavManager, consumed only by NavApp.
- navOptions: NavOptionsBuilder.() -> Unit enabling customization of navigation behavior.
- Applied sensible defaults.

```kotlin
sealed interface NavAction {

    data class To(
        val target: NavTarget,
        val navOptions: NavOptionsBuilder.() -> Unit = {
            launchSingleTop = true
            restoreState = true
        }
    ) : NavAction

    data class ResetTo(
        val target: NavTarget,
        val navOptions: NavOptionsBuilder.() -> Unit = {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    ) : NavAction

    data class PopTo(
        val target: NavTarget,
        val navOptions: NavOptionsBuilder.() -> Unit = { }
    ) : NavAction

    data object Up : NavAction
    data object PopToRoot : NavAction
}
```

### NavManager

- Singleton injected via Hilt.
- Only source of navigation commands and current destination.
    - Only NavApp calls updateCurrentTarget() → one-way data flow
    - All UI and ViewModels use navigateTo(), resetRoot(), etc.

```kotlin
@Singleton
class NavManager @Inject constructor() : Navigator {
    private val _commands = MutableSharedFlow<NavAction>(extraBufferCapacity = 1)
    override val commands = _commands.asSharedFlow()

    private val _currentTarget = MutableStateFlow<NavTarget?>(value = null)
    override val currentTarget = _currentTarget.asStateFlow()

    override fun updateCurrentTarget(target: NavTarget?) {
        _currentTarget.value = target
    }

    override fun navigateTo(
        target: NavTarget,
        navOptions: NavOptionsBuilder.() -> Unit
    ) = _commands.tryEmit(NavAction.To(target, navOptions))

    override fun resetRoot(
        target: NavTarget,
        navOptions: NavOptionsBuilder.() -> Unit
    ) = _commands.tryEmit(NavAction.ResetTo(target, navOptions))

    override fun popTo(
        target: NavTarget,
        navOptions: NavOptionsBuilder.() -> Unit
    ) = _commands.tryEmit(NavAction.PopTo(target, navOptions))

    override fun navigateUp() = _commands.tryEmit(NavAction.Up)
    override fun popToRoot() = _commands.tryEmit(NavAction.PopToRoot)
}
```

### NavGraph

- Navigation graphs are pure functions - no @Composable, no navController passed around.

```kotlin
fun NavGraphBuilder.mainNavGraph(snackbarHostState: SnackbarHostState) {
    composable<NavTargetRoot.Shabbat> { ShabbatScreen(snackbarHostState) }
    composable<NavTargetTop.Settings> { SettingsScreen() }
}
```

### 7. NavApp

- The only place that touches NavController.
- Syncs real navigation state → NavManager.

```kotlin
@Composable
fun NavApp(
    modifier: Modifier,
    navigator: Navigator,
    snackbarHostState: SnackbarHostState,
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(currentBackStackEntry) {
        navigator.syncBackStackWithNavigator(currentBackStackEntry)
    }

    LaunchedEffect(Unit) {
        navigator.collectNavigationCommands(navController)
    }

    val startDestination = NavTargetBottom.Shabbat

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        mainNavGraph(snackbarHostState)
    }
    //...
}
```

### UI Components Pure & Reusable

- Zero knowledge of NavController. Zero strings.

```kotlin
@Composable
fun NavBarIcon(
    isSelected: Boolean,
    item: NavItem,
    badgeCount: Int? = null,
) {
    BadgedBox(badge = { NavBarBadge(badgeCount) }) {
        UiIconImage(
            icon = if (isSelected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.title,
        )
    }
}
```

```kotlin
@Composable
fun NavBarBadge(count: Int? = null) {
    val displayCount = count?.takeIf { it > 0 } ?: return

    Badge(
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary,
    ) {
        Text(text = if (displayCount > 99) stringResource(R.string.display_count_max) else displayCount.toString())
    }
}
```

An Android app built with Jetpack Compose, Kotlin Coroutines, and MVI, focused on demonstrating
modern Android architecture and state management.
The app explores different architectural patterns for educational purposes, and intentionally applies some
over-engineered solutions to showcase design trade-offs and scalability.

## Table of Contents

1. [shabbat times app](#1--shabbat-app)
    1. [Current State of the App](#-current-state-of-the-app)
    2. [Future Work](#-future-work)
2. [MVI Architecture Overview](#2--mvi-architecture-overview)
    1. [MVI Terminology Mapping](#mvi-terminology-mapping)
3. [Navigation](#3--navigation)
4. [Permissions Management](#4--permissions-management)

---

## 1. 🕯 shabbat times app

### 📖 Overview

The Shabbat App is a lightweight, single-screen application that displays the upcoming
candle-lighting time and Havdalah time based on accurate solar data.
It automatically computes these times using real-world sunset data fetched via a public REST API and
applies the appropriate halachic offsets (+42 min / −18 min).

### 📸 Screenshots

- Screenshots reflect the current UI direction.
- Some interactions and data flows are still under active development.

<p align="start">
  <img src="docs/home_screen.png" width="180" />
  <img src="docs/search_auto_dark.png" width="180" />
  <img src="docs/home_screen_light.png" width="180" />
  <img src="docs/search_auto_light.png" width="180" />
</p>

---

### 🏛️ Architecture

The app is built using modern Android architecture principles and libraries:

#### 🔵 1. UI — Jetpack Compose

- Example:
```kotlin
@Composable
fun ShabbatScreen() {
    val shabbatViewModel: ShabbatViewModel = hiltViewModel()
    val shabbatState by shabbatViewModel.state.collectAsStateWithLifecycle()

    val searchViewModel: SearchViewModel = hiltViewModel()
    val searchUiState by searchViewModel.state.collectAsStateWithLifecycle()

    HandlePermissions(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ),
        permissionState = shabbatState.permission,
        dispatch = shabbatViewModel::dispatch,
    )

    val context = LocalContext.current

    when (shabbatState.data) {
        is ShabbatDataState.Idle    -> LoadingScreen()

        is ShabbatDataState.Loading -> LoadingScreen()

        is ShabbatDataState.Success -> ShabbatContent(
            shabbatState = shabbatState,
            shabbatDispatch = shabbatViewModel::dispatch,

            searchUiState = searchUiState,
            searchDispatch = searchViewModel::dispatch,
        )

        is ShabbatDataState.Failure -> FailureScreen(
            message = (shabbatState.data as ShabbatDataState.Failure).message,
            onRetry = { shabbatViewModel.dispatch(ShabbatDataEvent.Load) },
        )
    }

    LaunchedEffect(shabbatState.permission) {
        if (Debug.enabled) { Log.d("ShabbatScreen", "$shabbatState") }
        shabbatViewModel.effects.collect { effect ->
            if (Debug.enabled) { Log.d("ShabbatScreen", "$effect") }

            when (effect) {
                is AppEffect.Shabbat.OpenAppSettings -> context.openAppSettings()
                else -> Unit
            }
        }
    }
}
```

#### 🔵 2. DI — Dagger Hilt

#### 🔵 3. ViewModel — MVI (and experimental MVVM)

- MVI

```kotlin
@HiltViewModel
class ShabbatViewModel @Inject constructor(
  private val shabbatRepository: ShabbatRepository,
  private val cityRepository: CityRepository,
) : ViewModel() {
    private val _state: MutableStateFlow<ShabbatUiState> =
        MutableStateFlow(value = ShabbatUiState())
    val state: StateFlow<ShabbatUiState> = _state.asStateFlow()

    private val _effects: MutableSharedFlow<AppEffect> = MutableSharedFlow(extraBufferCapacity = 20)
    val effects: SharedFlow<AppEffect> = _effects.asSharedFlow()

    init {
        handleEffects()
        _effects.tryEmit(AppEffect.Shabbat.LoadData)
    }

    fun dispatch(event: AppEvent) {
        _state.update { current ->
            when (event) {
                is ShabbatDataEvent -> event.reducer reduce current
                is PermissionEvent  -> event.reducer reduce current
                is LocationEvent    -> event.reducer reduce current
                else                -> current
            }
        }

        when (event) {
            is ShabbatDataEvent.Load                -> _effects.tryEmit(AppEffect.Shabbat.LoadData)
            is PermissionEvent.RequestedAppSettings -> _effects.tryEmit(AppEffect.Shabbat.OpenAppSettings)

            else                                    -> Unit
        }
    }

    private fun handleEffects() {
        viewModelScope.launch {
            _effects.collect { effect ->
                when (effect) {
                    is AppEffect.Shabbat.LoadData   -> {
                        if (Debug.enabled) Log.d("ShabbatVM", "→ Processing LoadData effect")
                        loadData()
                    }

                    is AppEffect.Shabbat.LoadFailed -> {
                        handleShabbatLoadFailed(effect)
                        if (Debug.enabled) Log.d(
                            "ShabbatVM",
                            "→ Processing LoadFailed: ${effect.error.message}"
                        )
                    }

                    else                            -> if (Debug.enabled) Log.w(
                        "ShabbatVM",
                        "Unhandled effect: $effect"
                    )
                }
            }
        }
    }

    private fun loadData() {
        if (_state.value.data is ShabbatDataState.Loading) {
            if (Debug.enabled) Log.d("ShabbatVM", "Load already in progress – skipping")
            return
        }

        _state.update { current ->
            current.copy(data = ShabbatDataState.Loading)
        }

        viewModelScope.launch {
            shabbatRepository
                .getHalachicTimesForCities(cityRepository.cities)
                .map { it.toLoadedEvent() }
                .catch { exception ->
                    if (Debug.enabled) Log.e("ShabbatVM", "Load failed", exception)

                    dispatch(
                        ShabbatDataEvent.Loaded.Failure(
                            message = exception.message ?: "Unknown error",
                            cause = exception
                        )
                    )
                    _effects.tryEmit(AppEffect.Shabbat.LoadFailed(exception))
                }
                .collectLatest { event ->
                    if (Debug.enabled) Log.d("ShabbatVM", "Load success")
                    dispatch(event)
                }
        }
    }
// ...
}
```

- MVVM

```kotlin
@HiltViewModel
class ShabbatViewModelMVVM @Inject constructor(
    private val repository: ShabbatRepository
) : ViewModel() {
    private val _halachicTimes = MutableStateFlow(HalachicTimesDisplay())
    val halachicTimes: StateFlow<HalachicTimesDisplay> = _halachicTimes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val debounceJob = SupervisorJob()

    init { refresh() }

    fun retry() {
        debounceJob.cancelChildren()
        viewModelScope.launch(debounceJob) {
            delay(300)
            refresh()
        }
    }

    private fun refresh() {
        if (_isLoading.value) {
            if (Debug.enabled) Log.d("ShabbatMVVM", "Refresh already in progress – skipping duplicate call")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            runCatching {
                repository.getHalachicTimes()
            }.fold(
                onSuccess = { result ->
                    if (Debug.enabled) Log.d("ShabbatMVVM", "Load success")
                    _halachicTimes.value = (result as NetworkResult.Success).data
                },
                onFailure = { exception ->
                    val msg = exception.message ?: "Failed to load Halachic times"
                    if (Debug.enabled) Log.e("ShabbatMVVM", msg, exception)
                    _errorMessage.value = msg
                }
            )

            _isLoading.value = false
        }
    }
}
```

#### 🔵 4. Networking — OkHttp + Retrofit

- Example:
  ```kotlin
  @Provides
  @Singleton
  fun provideRetrofit(): Retrofit {
      val client = OkHttpClientFactory.create(Debug.enabled)
      val contentType = "application/json".toMediaType()

      return Retrofit.Builder()
          .baseUrl(BASE_SUNRISE_SUNSET)
          .client(client)
          .addConverterFactory(JsonConfig.json.asConverterFactory(contentType))
          .build()
  }
  ```

#### 🔵 5. Data Layer — Clean architecture with repository abstraction

```kotlin
interface ShabbatRepository {
    suspend fun getSolarTimes(date: LocalDate, city: City): NetworkResult<SolarTimes>
    suspend fun getHalachicTimes(city: City): NetworkResult<HalachicTimesDisplay>
    suspend fun getHalachicTimesForCities(cities: Flow<List<City>>): Flow<List<NetworkResult<HalachicTimesDisplay>>>
}
```

```kotlin
interface CityRepository {
    val cities: Flow<List<City>>
    suspend fun addCity(city: City)

    suspend fun geocodeAutocomplete(query: String): Flow<List<City>>
    suspend fun geocodeForward(query: String): Flow<City?>
    suspend fun geocodeReverse(latitude: Double, longitude: Double): City?
}
```

#### 🔵 6. KotlinX Serialization — For DTO parsing

#### 🔵 7. Domain Layer — Strongly typed models (LocalDate, LocalTime)

### 🌅 Solar Times API

The app uses the public API at https://sunrisesunset.io/ to fetch daily solar times (primarily
sunset).
The default location is currently Jerusalem, but dynamic user location is on the roadmap.

#### 🟢 1. Example API response (trimmed for relevance):

```kotlin
{
    "results": {
    "date": "2025-04-12",
    "sunset": "11:56:00 AM"
},
    "status": "OK"
}
```

#### 🟢 2. Retrofit endpoint definition:

```kotlin
@GET("json")
suspend fun getSolarTimes(
    @Query("lat") lat: Double = Cities.JERUSALEM.lat,
    @Query("lng") lng: Double = Cities.JERUSALEM.lng,
    @Query("timezone") timezone: String = Cities.JERUSALEM.timezone,
    @Query("time_format") timeFormat: Int = 24,
    @Query("date") date: String? = null,
): SolarTimesDto
```

```kotlin
    @GET("autocomplete")
    suspend fun autocomplete(
        @Query("text") queryText: String,
        @Query("filter") countryFilter: String? = null,
        @Query("type") resultType: String? = "city",

        @Query("limit") maxResults: Int = 5,
        @Query("lang") preferredLanguage: String = Locale.getDefault().language,
        @Query("format") format: String = "json",
        @Query("apiKey") apiKey: String = BuildConfig.GEOAPIFY_API_KEY,
    ): GeoapifyResponseDto
```

#### 🟢 3. DTO and domain mapping:

```kotlin
@Serializable
data class SolarTimesResponseDto(
    val results: SolarTimesResultDto = SolarTimesResultDto(),
    val status: String = "",
)

@Serializable
data class SolarTimesResultDto(
    val date: String = "",
    val sunset: String = "",
)

@Serializable
data class GeoapifyResponseDto(
    val results: List<GeoapifyResultDto>? = null,
    val status: String? = null,
    val query: GeoapifyQuery? = null
)

@Serializable
data class GeoapifyResultDto(
  // ────────────────────────────────────────────────
  // Core location
  // ────────────────────────────────────────────────
  @SerialName("lat")          val latitude: Double? = null,
  @SerialName("lon")          val longitude: Double? = null,
  val timezone: GeoapifyTimezone? = null,
// ...
)
```

#### 🟢 4. The repository fetches specific solar times for:

- upcoming Friday → used to compute candle lighting.
- upcoming Saturday → used to compute Havdalah.
  ```kotlin
  fun upcomingCandleLightingDate(): LocalDate = now().nextOrTodayDayOfWeek(DayOfWeek.FRIDAY)
  fun upcomingHavdalahDate(): LocalDate = now().nextOrTodayDayOfWeek(DayOfWeek.SATURDAY)
  
  fun LocalDate.nextOrTodayDayOfWeek(target: DayOfWeek): LocalDate { ... }
  ```

### 🕒 Halachic Times

Halachic times (zmanim) represent meaningful moments defined in Jewish law.

#### 🟡 1. Two key calculations are implemented:

These represent widely used halachic opinions, but other variants may be introduced later:

- Candle Lighting — 18 minutes before Friday sunset.
- Offset constant: HILUCH_MIL_MINUTES = 18L.


- Havdalah — 42 minutes after Saturday sunset.
- Offset constant: TZEIT_HAKOCHAVIM_MINUTES = 42L.

#### 🟡 2. Domain model

```kotlin
data class HalachicTimes(
    val candleLightingTime: LocalTime,
    val candleLightingDate: LocalDate,
    val havdalahTime: LocalTime,
    val havdalahDate: LocalDate,
)
```

#### 🟡 3. Display model (used only by UI):

```kotlin
data class HalachicTimesDisplay(
    val candleLightingTime: String = "",
    val candleLightingDate: String = "",
    val havdalahTime: String = "",
    val havdalahDate: String = "",
)
```

#### 🟡 4. Conversions are done via extensions:

```kotlin
fun HalachicTimes.toDisplay(context: Context): HalachicTimesDisplay
```

### 👤 User Preferences

#### 🔴 1. Current version uses a hard-coded Jerusalem location, but the architecture anticipates:

- dynamic device location
- user-selectable city presets
- multiple zmanim opinions

#### 🔴 2. User preferences include:

- 12/24-hour clock awareness, via UserPreferences.is24HourFormat().

### 📅 Date & Time Handling

#### 🟣 1. To ensure accuracy and avoid parsing issues:

- All DTO fields (strings) are immediately converted into LocalDate and LocalTime domain objects.
- Calculations (± minutes) are performed only on strongly typed data.
- Formatting back to strings happens only at UI-binding time.

#### 🟣 2. Examples:

```kotlin
val API_DATE_PARSER = DateTimeFormatter.ISO_LOCAL_DATE
val API_TIME_PARSER_24 = DateTimeFormatter.ISO_LOCAL_TIME
val HEBREW_DATE_FORMATTER = DateTimeFormatter.ofPattern(HEBREW_DATE_PATTERN)
```

#### 🟣 3. Utility extensions include:

```kotlin
fun LocalDate.nextOrTodayDayOfWeek(target: DayOfWeek): LocalDate
fun upcomingCandleLightingDate(): LocalDate
fun upcomingHavdalahDate(): LocalDate
fun LocalTime.toDisplayString(context: Context): String
fun LocalDate.toDisplayString(): String
```

### 🏁 Current State of the App

#### 🟠 1. The app currently includes:

- Initial Shabbat screen with:
    - hardcoded Jerusalem location
    - candle lighting date & time
    - Havdalah date & time
    - Fully functional REST API integration
    - Domain-accurate halachic time calculations
    - Strong type-safety with Kotlin time models
    - First stable working implementation

### 🔜 Future Work

#### ⚫ 1. Planned improvements:

- Dynamic user location.
- Multiple halachic opinions (e.g., 40/72 minutes).
- Offline caching of zmanim.

---

## 2. 🔄 MVI Architecture Overview

This project follows pure unidirectional MVI with a few naming choices that avoid Android-specific
confusion (e.g., "Event" instead of "Intent").

### Core MVI Concepts

- **AppState**  
  Single immutable source of truth. A data class that combines all feature-specific sub-states.

- **AppEvent**  
  Represents user intentions or external triggers. Implemented as a sealed hierarchy.  
  Each event is **self-reducing**: it carries its own pure reducer via the `Reducible<S>` interface.

    - **Reducer**  
      A pure function `(oldState: S) → newState: S` that computes the next sub-state.  
      Completely side-effect-free.

    - **Reducible&lt;S&gt;**  
      A marker interface that events implement to declare:  
      *"I know how to reduce a specific sub-state of type S."*  
      This enables the clean, boilerplate-free pattern where events reduce themselves.

- **AppEffect**  
  Represents imperative, one-shot side effects that cannot be expressed purely (e.g., starting
  background loops, network requests, showing toasts, navigation, logging).

## Data Flow

### Pure MVI Cycle (Concise)

```kotlin
UI → dispatch(Event)
→ pure state reduction → new AppState
→ (optional) emit AppEffect
→ effect collector handles imperative work
→ may dispatch new Events
→ UI updates
```

### Step-by-Step

1. User Interaction

- The UI calls viewModel.dispatch(event) with an AppEvent (e.g. PermissionEvent.Request)).

2. Pure State Reduction

- The event, being Reducible&lt;S&gt;, carries its own pure reducer.
- The ViewModel applies it to the relevant sub-state and updates the immutable

  ```kotlin
  fun dispatch(event: AppEvent) {
        _state.update { current ->
            when (event) {
                is ShabbatDataEvent -> event.reducer reduce current
                is PermissionEvent  -> event.reducer reduce current
                is LocationEvent    -> event.reducer reduce current
            }
        }
  }
  ```

- The UI automatically recomposes with the new state.

3. Side Effects (If Needed)

- If the state transition requires real-world work (network request, navigation, toast, etc.), the ViewModel emits an AppEffect:

   ```kotlin
   _effects.tryEmit(AppEffect.Shabbat.OpenAppSettings)
   ```

4. Imperative Work

- A single coroutine (launched once in init) collects all effects and executes the necessary
  actions:

  ```kotlin
  effects.collect { effect -> 
      when (effect) {
          AppEffect.Shabbat.LoadData -> loadShabbatData() 
      // ... 
      } 
  }
  ```

5. Feedback Loop

- These actions may dispatch new events (e.g. ShabbatEvent.Loaded.Success / Failure), which restart the cycle.

### In Plain English

- The user does something → an Event is created and sent to the ViewModel.
- The Event knows exactly how to calculate the new State (pure, no side effects).
- The ViewModel updates the State → the UI refreshes automatically.
- If something “real” needs to happen (load data, show a toast), the ViewModel sends
  an Effect.
- One permanent listener in the ViewModel catches all Effects and performs the actual work.
- That work can create new Events, keeping everything flowing in one direction.
- This unidirectional, pure MVI flow ensures predictability, testability, and easy reasoning about
  application behavior.

### MVI Terminology Mapping

| Project Term      | Common MVI Equivalent           | Notes                                                                                                                                                                                        |
|-------------------|---------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Event             | Intent / Action                 | User intention or external trigger that drives state change                                                                                                                                  |
| State             | State / Model                   | Single immutable source of truth for the UI                                                                                                                                                  |
| Effect            | Effect / SideEffect / Command   | Imperative one-shot actions (network, start loop, toast)                                                                                                                                     |
| dispatch          | send / accept / dispatch        | Public entry point to send an Event into the ViewModel                                                                                                                                       |
| Reducer           | Reducer (central function)      | Pure function: (oldState, event) → newState (or sub-state)                                                                                                                                   |
| Reducible         | no direct equivalent            | Marker interface saying "this Event carries its own reducer". Also known as self-reducing events, reducer-carrying actions, or fat actions. Avoids central when switch — clean and scalable. |
| Event + Reducible | Central when (event) in reducer | preferred variant for readability and no boilerplate                                                                                                                                         |

### Why separate Events from Effects?

- `Events` are declarative: "I want to load Shabbat times", "Start breathing".
    - They only describe intent and how the state should change.
- `Effects` are imperative: "Actually perform the network request", "Start the breathing loop".
    - They are executed outside the pure reducer, keeping state transitions predictable and
      testable.

### Benefits

- This unidirectional, pure MVI flow ensures predictability, testability, and easy reasoning about
  application behavior.
- Pure reducers → easy unit testing (just input event → expected state)
- All side effects centralized in one collector → consistent error handling, logging, retry
- Clear responsibilities → UI stays dumb, ViewModel stays focused on orchestration
- Scalable → new async flows (e.g., analytics, push notifications) follow the same pattern

### Example: Shabbat loading

1. UI or init → `dispatch(ShabbatEvent.Load)`
2. Reducer → `ShabbatUiState.Loading`
3. ViewModel emits `AppEffect.Shabbat.LoadData`
4. Effect collector → calls repository → dispatches `ShabbatEvent.Loaded.Success/Failure`
5. Reducer → `ShabbatUiState.Success` or `Failure`
6. UI updates automatically

---

## 3. 🧭 Navigation

A **type-safe, scalable, testable, and production-proven** navigation system built for modern
Android apps using Jetpack
Compose + Navigation + Hilt + Kotlin Serialization.

### 🏛️ Core Principles

| Principle                      | Implementation & Benefit                                                                            |
|--------------------------------|-----------------------------------------------------------------------------------------------------|
| **Reusable UI**                | No `NavController` in UI → UI components stay dumb and reusable                                     |
| **Type-safe**                  | Sealed interfaces + `@Serializable` + `hasRoute<T>()` → no strings, no crashes                      |
| **flows through `NavManager`** | All navigation goes through `NavManager` No other class talks to `NavController` or reads backstack |
| **Modular & scalable**         | Separate graph functions per feature → easy to maintain, test, and extend                           |
| **Deep link safe**             | Full support out of the box — no extra code needed                                                  |
| **Testable**                   | `NavManager` is Hilt-injectable singleton → easy to mock in unit & UI tests                         |

### 🏛️ Core Components

| Component          | Responsibility                                                                             |
|--------------------|--------------------------------------------------------------------------------------------|
| 1. `NavTarget`     | Sealed hierarchy of **all app destinations** — the heart of type-safe navigation           |
| 2. `NavItem`       | Visual + behavioral representation of a navigation item (icon, title, badge, role)         |
| 3. `NavRole`       | Defines where the item appears: bottom tab, top navigation, action button, etc.            |
| 4. `NavAction`     | Sealed class representing navigation commands (`To`, `Up`, `ResetTo`, etc.)                |
| 5. `NavManager`    | Singleton brain: emits commands, exposes current destination, fully injectable             |
| 6. `*.NavGraph.kt` | Feature-isolated graph builders (`authNavGraph`, `bottomNavGraph`, `alertsNavGraph`, etc.) |
| 7. `NavApp`        | Root composable — the **only** place that talks to `NavController`                         |
| 8. UI Components   | `NavBarBottom`, `NavBarTop`, `NavBarIcon`, ... — pure UI, zero navigation logic            |

### 🔵 1. NavTarget — Type-Safe Destinations

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

                this?.destination?.hasRoute<NavTargetBottom.Shabbat>() == true -> NavTargetBottom.Shabbat
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

### 🔵 2. NavItem - Navigation UI Metadata

- Encapsulates everything needed to render a navigation item in bottom bar, or top bar.

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
        title = "Settings",
        selectedIcon = UiIcon.Resource(R.drawable.settings_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.settings_outlined_24),
        role = NavRole.TOP_ACTION,
    )
    // ...
}
```

### 🔵 3. NavRole - Placement & Behavior

- Defines where and how a NavItem should be displayed.

```kotlin
enum class NavRole {
    BOTTOM_TAB,
    TOP_NAVIGATION,
    TOP_ACTION,
    // ...
}
```

### 🔵 4. NavAction - Navigation Commands

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

### 🔵 5. NavManager - The Brain

- Singleton injected via Hilt.
- Only source of navigation commands and current destination.
    - Only NavApp calls updateCurrentTarget() → one-way data flow
    - All UI and ViewModels use navigateTo(), resetRoot(), etc.

```kotlin
@Singleton
class NavManager @Inject constructor() {
    private val _commands = MutableSharedFlow<NavAction>(extraBufferCapacity = 1)
    val commands = _commands.asSharedFlow()

    private val _currentTarget = MutableStateFlow<NavTarget?>(value = null)
    val currentTarget = _currentTarget.asStateFlow()

    internal fun updateCurrentTarget(target: NavTarget?) {
        _currentTarget.value = target
    }

    fun navigateTo(target: NavTarget) = _commands.tryEmit(NavAction.To(target))
    fun navigateUp() = _commands.tryEmit(NavAction.Up)
    fun resetRoot(target: NavTarget) = _commands.tryEmit(NavAction.ResetTo(target))
    fun popTo(target: NavTarget) = _commands.tryEmit(NavAction.PopTo(target))
    fun popToRoot() = _commands.tryEmit(NavAction.PopToRoot)
}
```

### 🔵 6. NavGraph — Modular & Clean

- Navigation graphs are pure functions — no @Composable, no navController passed around.

```kotlin
fun NavGraphBuilder.mainNavGraph(navigator: Navigator) {
    composable<NavTargetBottom.Shabbat> {
        ShabbatScreen()
    }

    composable<NavTargetTop.Settings> {
        FailureScreen(message = "Coming Soon") {
            navigator.navigateUp()
        }
    }
}
```

### 🔵 7. NavApp — The Bridge

- The only place that touches NavController.
- Syncs real navigation state → NavManager.

```kotlin
@Composable
fun NavApp(
    modifier: Modifier,
    state: AppModel,
    onEvent: (AppEvent) -> Unit,
    navigator: Navigator,
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(currentBackStackEntry) {
        navigator.syncBackStackWithNavigator(currentBackStackEntry)
    }

    LaunchedEffect(Unit) {
        navigator.collectNavigationCommands(navController)
    }

    val startDestination = NavTargetBottom.Home

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        mainNavGraph(
            state = state,
            onEvent = onEvent,
        )
    }

    if (Debug.enabled) {
        LaunchedEffect(navController) {
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                Log.d(
                    "navController",
                    "Current destination: ${navController.currentDestinationName()}"
                )
            }
        }
    }
}
```

### 🔵 8. UI Components Pure & Reusable

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

@Composable
fun NavBarBadge(count: Int? = null) {
    val displayCount = count?.takeIf { it > 0 } ?: return

    Badge(
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary,
    ) {
        Text(text = if (displayCount > 99) "99+" else displayCount.toString())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBarTop(
    navItems: List<NavItem>,
    navigator: Navigator,
    currentNavTarget: NavTarget? = null,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val (topNavigationItem, topActionItems) = navItems.extractTopBarItems()

    TopAppBar(
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            subtitleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        title = { Text(text = currentNavTarget?.simpleName() ?: "None") },
        navigationIcon = {
            topNavigationItem?.let {
                IconButton(onClick = { navigator.navigateUp() }) {
                    NavBarIcon(
                        isSelected = currentNavTarget == it.target,
                        badgeCount = null,
                        item = it,
                    )
                }
            }
        },
        actions = {
            topActionItems.forEach { item ->
                val onItemClick = { navigator.navigateTo(item.target) }

                IconButton(onClick = { onItemClick() }) {
                    NavBarIcon(
                        isSelected = currentNavTarget == item.target,
                        badgeCount = null,
                        item = item,
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}
```

---

## 4. 🚦 Permissions Management

- The code implements a robust permission system for requesting and managing Android permissions, specifically fine and coarse location access.
- This is crucial for the app to fetch the user's location and calculate accurate Shabbat times.

### Key Features

- Checks whether required permissions are already granted (avoids unnecessary requests)
- Launches the native Android permission dialog only when needed

- Correctly distinguishes between:
    - temporary denials (shows rationale dialog)
    - permanent denials ("Don't ask again" / multiple denials → guides to app settings)

- Fully asynchronous using Kotlin coroutines (`suspend` functions) — no UI blocking
- Clean state management with sealed interfaces (`PermissionState`, `PermissionResult`, `PermissionEvent`) + reducers

- Provides user-friendly in-app explanations and retry / settings navigation flows
- Reusable abstraction (`PermissionHandler` interface) — easy to adapt for camera, storage, notifications, etc.
- Built-in debug logging and permission status checks

### Core Components

| Component                 | Role                                                                       | Type               |
|---------------------------|----------------------------------------------------------------------------|--------------------|
| PermissionHandler         | Suspendable API to request permissions and handle result callbacks         | Interface + Impl   |
| PermissionResult          | Domain-level outcome of a permission request (what happened)               | Sealed interface   |
| PermissionState           | UI/presentation state (what to show right now: dialog, nothing, etc.)      | Sealed interface   |
| PermissionEvent           | User/system intents and results that drive state transitions               | Sealed interface   |
| HandlePermissions         | Composable orchestrator: reacts to state, shows dialogs, triggers requests | Composable         |
| rememberPermissionHandler | Creates and remembers the handler + ActivityResultLauncher                 | Composable factory |

### High-Level Flow

1. User triggers a request in Composable (`ShabbatScreen`)
2. The app checks the current permission state. (`HandlePermissions`)
3. If not granted, it triggers a dialog. (`ExplanatoryDialog`)
4. Based on the user's response, it updates the state (granted, denied with rationale, or permanently denied).
5. Dialogs guide the user on next steps.
6. Events are dispatched to a ViewModel, which updates the app's state and may trigger effects (e.g., loading data or opening settings).

### Visualized Flow

```markdown
Start
│
└─ 👤 Requests (ShabbatViewModel.dispatcher)
   │
   ├─ event Request ─ state Requesting
   │
   └─ 🤖 The `HandlePermissions` composable uses `rememberPermissionHandler` to check whether the permissions are already granted.
      │
      ├─ Yes
      │  └── result Granted ─ event AllGranted ─ state Granted ✅
      │
      └─ No (Launch system dialog 💬)
         │
         ├─ 👤 Allows all 
         │  └── result Granted ─ event AllGranted ─ state Granted ✅
         │
         └─ 👤 Denies (Show rationale dialog 💬)
            │
            └── result Explain ─ event DeniedWithRationale ─ state Denied ❗
                       │
                       ├─ 👤 Allows  
                       │  └── event AcceptedRationale ─ state Requesting
                       │
                       ├─ 👤 Cancels
                       │  └── event DismissedRationale ─ state Idle ❌❗
                       │
                       └─ 👤 Denies 💬 (Show "go to settings" dialog)
                          │
                          └── result Blocked ─ event DeniedPermanently ─ state DeniedPermanently 🚫
                                     │
                                     ├─ 👤 Opens settings
                                     │  └── event RequestedAppSettings ─ state Idle ─ effect OpenAppSettings ✨
                                     │
                                     └─ 👤 Cancels
                                        └── event DismissedRationale ─ state Idle ❌🚫
```

### Structured Components

- The code is modular, divided into interfaces, classes, composables, and sealed hierarchies. Below is a breakdown by component.

#### 🟢 1. PermissionHandler Interface

- Definition: A simple interface for requesting permissions asynchronously.
- Key Method: suspend fun request(permissions: List<String>): PermissionResult
- Takes a list of permission strings (e.g., Manifest.permission.ACCESS_FINE_LOCATION).
- Returns a PermissionResult (sealed interface: Granted, Explain, or Blocked).

- This acts as the entry point for permission requests.

#### 🟢 2. PermissionHandlerImpl Class

- Dependencies:
    - isGranted: Lambda to check if a permission is already granted (uses ContextCompat.checkSelfPermission).
    - shouldShowRationale: Lambda to check if a rationale should be shown (uses ActivityCompat.shouldShowRequestPermissionRationale).
    - launch: Lambda to start the permission request dialog (via ActivityResultLauncher).

- Internal State: A CancellableContinuation to handle coroutine suspension and resumption.

- request() Function:
    - Uses suspendCancellableCoroutine to pause until the result is available.
    - Filters out already granted permissions.
    - If all are granted, resumes immediately with Granted.
    - Otherwise, launches the request and waits.

- onResult() Function:
    - Called when the system returns permission results (a map of permission to boolean granted status).
    - Categorizes results: granted, denied, permanently denied.
    - Resumes the coroutine with the appropriate PermissionResult.
    - Clears the continuation to allow future requests.

- This class bridges the Android permission API with coroutines.

```kotlin
class PermissionHandlerImpl(
    private val isGranted: (String) -> Boolean,
    private val shouldShowRationale: (String) -> Boolean,
    private val launch: (Array<String>) -> Unit,
) : PermissionHandler {
    private var continuation: CancellableContinuation<PermissionResult>? = null

    override suspend fun request(permissions: List<String>): PermissionResult =
        suspendCancellableCoroutine { cont ->
            check(continuation == null) { "Permission request already in progress" }

            val missing = permissions.filterNot(isGranted)

            if (missing.isEmpty()) {
                cont.resume(PermissionResult.Granted)
                return@suspendCancellableCoroutine
            }

            continuation = cont
            launch(missing.toTypedArray())

            cont.invokeOnCancellation {
                continuation = null
            }
        }

    fun onResult(result: Map<String, Boolean>) {
// ...
    }
}
```

#### 🟢 3. rememberPermissionHandler Composable

- Purpose: Creates and remembers a PermissionHandlerImpl instance in Compose.
- Key Elements:
    - Uses rememberLauncherForActivityResult with RequestMultiplePermissions contract to handle permission callbacks.
    - Initializes the handler with context-specific lambdas for checking grants and rationale.

- Returns: The PermissionHandler instance, memoized for recomposition efficiency.

- This makes the handler Compose-aware and lifecycle-safe.

```kotlin
@Composable
fun rememberPermissionHandler(): PermissionHandler {
    val context = LocalContext.current
    lateinit var handler: PermissionHandlerImpl

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        handler.onResult(result)
    }

    handler = remember(context, launcher) {
        PermissionHandlerImpl(
            isGranted = { perm ->
                ContextCompat.checkSelfPermission(
                    context,
                    perm
                ) == PackageManager.PERMISSION_GRANTED
            },
            shouldShowRationale = { perm ->
                val activity = context as Activity
                ActivityCompat.shouldShowRequestPermissionRationale(activity, perm)
            },
            launch = launcher::launch
        )
    }

    return handler
}
```

#### 🟢 4. HandlePermissions Composable

- Parameters:
    - permissions: List of permissions to request.
    - permissionState: Current PermissionState (from ViewModel).
    - dispatch: Function to send PermissionEvents to the ViewModel.

- Behavior:
    - Uses LaunchedEffect to request permissions when state is Requesting.
    - Based on result, dispatches events like AllGranted, DeniedWithRationale, or DeniedPermanently.

- Renders dialogs:
    - For Denied: Explains need for location and offers "Allow" (triggers re-request) or dismiss.
    - For DeniedPermanently: Prompts to open settings.

- Debug logging for permission status.

- This composable integrates the handler with UI feedback.

```kotlin
@Composable
fun HandlePermissions(
    permissions: List<String>,
    permissionState: PermissionState,
    dispatch: (PermissionEvent) -> Unit,
) {
    val permissionHandler = rememberPermissionHandler()
    val context = LocalContext.current

    LaunchedEffect(permissionState) {
        if (permissionState == PermissionState.Requesting) {
            val result = permissionHandler.request(permissions)

            when (result) {
                is PermissionResult.Granted ->
                    dispatch(PermissionEvent.AllGranted)

                is PermissionResult.Explain ->
                    dispatch(PermissionEvent.DeniedWithRationale)

                is PermissionResult.Blocked ->
                    dispatch(PermissionEvent.DeniedPermanently)
            }
        }
    }
// ...
}
```

#### 🟢 5. Sealed Interfaces

- PermissionState:
    - Idle: Init state.
    - Requesting: In progress.
    - Granted: All permissions approved.
    - Denied: Denied but can show rationale.
    - DeniedPermanently: User selected "Don't ask again" or denied multiple times.

- PermissionResult:
    - Granted: Success.
    - Explain(permissions): Denied, show why it's needed.
    - Blocked(permissions): Permanently denied.

- PermissionEvent:
    - Request: Start requesting.
    - AllGranted/DeniedWithRationale/DeniedPermanently: Update state based on result.
    - AcceptedRationale: User agrees after explanation, re-request.
    - DismissedRationale: User cancels, back to idle.
    - RequestedAppSettings: Open settings.
    - ReturnedFromAppSettings: Re-check after settings (triggers request).

- Each event includes a reducer lambda to update the app state immutably.

#### 🟢 6. Usage in ShabbatScreen Composable

- Injects ShabbatViewModel via Hilt.
- Collects state with collectAsStateWithLifecycle.
- Calls HandlePermissions with location permissions, current state, and dispatch function.
- Renders UI based on data state (e.g., loading screen).

- This shows integration in a real screen.

- See: [1. UI — Jetpack Compose](#-1-ui--jetpack-compose)

#### 🟢 7. ViewModel

- dispatch(event):
    - Updates _state using the event's reducer (for data, permission, or location events).
    - Emits effects for side actions (e.g., load data or open settings).

- This centralizes state updates and effects.
- See: [3. ViewModel — MVI (and experimental MVVM)](#-3-viewmodel--mvi-and-experimental-mvvm) 

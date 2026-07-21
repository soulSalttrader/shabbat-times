# Permissions Management

The app implements a robust permission system for requesting and managing Android location permissions, which is crucial for fetching the user's location and calculating accurate Shabbat times.

- Syncs permission state with real device state on app start.
- Correctly restores `Granted`, `Denied`, or `DeniedPermanently` after restart.

## Key Features

The following features ensure a smooth and reliable permission handling experience for the user.

- **App Start Sync:** Syncs real device permission state on app start — no stale `Idle` state after restart.
- **Avoids Redundant Requests:** Checks whether required permissions are already granted — avoids unnecessary requests.
- **Smart Native Dialogs:** Launches the native Android permission dialog only when needed.
- **Detailed Deny Tracking:** Correctly distinguishes between temporary denials (shows rationale) and permanent denials (guides to settings).
- **Independent Visibility:** Separates dialog visibility from permission state — dismissing a dialog never resets the underlying permission.
- **Lifecycle Awareness:** Re-checks permission when the user returns from system settings.
- **Asynchronous Execution:** Fully asynchronous using Kotlin coroutines — no UI blocking.
- **Clean State Management:** Uses sealed interfaces and reducers for predictable state transitions.
- **Abstraction:** Provides a reusable `PermissionHandler` abstraction that is easy to adapt for other permission types.

## Key Components

The permission system is built on a set of specialized components that manage state and system interactions.

| Component | Role | Type |
|-----------|------|------|
| `PermissionHandler` | Suspendable API to request, check, and handle permission results | Interface + Impl |
| `PermissionResult` | Domain-level outcome of a permission request | Sealed interface |
| `PermissionState` | Underlying permission state (Granted, Denied, DeniedPermanently...) | Sealed interface |
| `PermissionUiState` | UI state — combines `PermissionState` + `isDialogVisible` | Data class |
| `PermissionEvent` | User/system intents that drive state transitions, each with a reducer | Sealed interface |
| `HandlePermissions` | Orchestrator — syncs state on start, reacts to lifecycle, triggers requests | Composable |
| `PermissionDialogs` | Renders appropriate dialog based on `PermissionUiState` | Composable |
| `rememberPermissionHandler` | Creates and remembers handler + `ActivityResultLauncher` | Composable factory |

## High-Level Flow

The permission lifecycle follows a clearly defined path from application start to user authorization.

1. App starts → `HandlePermissions` syncs real device permission via `resolvePermissionEvent()`.
2. If already granted → GPS starts automatically.
3. If not granted → user taps GPS card → appropriate dialog shown based on permission state.
4. User responds → event dispatched → state updated via reducer.
5. System permission dialog shown if needed (`Requesting` state).
6. Based on result → `Granted`, `DeniedWithRationale`, or `DeniedPermanently`.
7. `DeniedPermanently` → settings dialog → user opens settings → `ReturnedFromAppSettings` re-checks on resume.

## Visualized Flow

The following diagram provides a visual representation of the permission request and state transition logic.

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

## Implementation Details

The code is modular, divided into interfaces, classes, composables, and sealed hierarchies.

### 1. PermissionHandler Interface

Interface for requesting and checking permissions asynchronously.

- Methods:
  - `suspend fun request(permissions: List<String>): PermissionResult`
  - `fun isGranted(permission: String): Boolean`
  - `fun shouldShowRationale(permission: String): Boolean`
- `request()` takes a list of permission strings and returns a `PermissionResult` (sealed interface: `Granted`, `Explain`, or `Blocked`).
- `isGranted()` and `shouldShowRationale()` allow checking permission state without triggering a request — used for syncing state on app start.

### 2. PermissionHandlerImpl Class

Bridges the Android permission API with coroutines using a `CancellableContinuation`.

- Dependencies:
  - `checkPermission`: Lambda to check if a permission is already granted (`ContextCompat.checkSelfPermission`).
  - `checkShouldShowRationale`: Lambda to check if rationale should be shown (`ActivityCompat.shouldShowRequestPermissionRationale`).
  - `launch`: Lambda to start the permission request dialog (via `ActivityResultLauncher`).

- Internal State: A `CancellableContinuation` to handle coroutine suspension and resumption.

- `request()` Function:
  - Uses `suspendCancellableCoroutine` to pause until the result is available.
  - Filters out already granted permissions.
  - If all are granted, resumes immediately with `Granted`.
  - Otherwise, launches the request and waits.

- `onResult()` Function:
  - Called when the system returns permission results (map of permission to boolean).
  - Categorizes results: granted, denied, permanently denied.
  - Resumes the coroutine with the appropriate `PermissionResult`.
  - Clears the continuation to allow future requests.

```kotlin
class PermissionHandlerImpl(
    private val checkPermission: (String) -> Boolean,
    private val checkShouldShowRationale: (String) -> Boolean,
    private val launch: (Array<String>) -> Unit,
) : PermissionHandler {
    override fun isGranted(permission: String): Boolean = checkPermission(permission)
    override fun shouldShowRationale(permission: String): Boolean = checkShouldShowRationale(permission)

    private var continuation: CancellableContinuation<PermissionResult>? = null

    override suspend fun request(permissions: List<String>): PermissionResult =
        suspendCancellableCoroutine { cont ->
            check(continuation == null) { "Permission request already in progress" }

            val missing = permissions.filterNot { isGranted(it) }

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
        val cont = continuation ?: return
        try {
            val denied = result.filterValues { !it }.keys.toList()
            val permanentlyDenied = denied.filterNot(checkShouldShowRationale)

            when {
                denied.isEmpty() -> {
                    cont.resume(PermissionResult.Granted)
                }

                permanentlyDenied.isNotEmpty() -> {
                    cont.resume(PermissionResult.Blocked(permissions = permanentlyDenied))
                }

                else -> {
                    cont.resume(PermissionResult.Explain(permissions = denied))
                }
            }
        } finally {
            continuation = null
        }
    }
}
```

### 3. Permission State Management

The system ensures that the UI accurately reflects the current permission status while maintaining a clean state model.

- `PermissionUiState` contains both `permission: PermissionState` and `isDialogVisible: Boolean` — separating dialog visibility from underlying permission state.
- Dismissing a dialog sets `isDialogVisible = false` when resetting `permission` — prevents state loss on dismiss.
- `PermissionState.Idle` — never asked. `PermissionState.Hidden` removed in favor of `isDialogVisible`.
- On app start, `resolvePermissionEvent()` extension on `PermissionHandler` checks real device permission and syncs repository state — ensures `Granted`/`Denied`/`DeniedPermanently` are correctly restored after restart.
- `ReturnedFromAppSettings` event re-triggers permission request when user returns from system settings.

### 4. HandlePermissions Composable

Orchestrates the permission request flow and reacts to lifecycle events.

- Parameters:
  - `permissions`: List of permissions to request.
  - `permissionState: PermissionUiState` — full UI state including `isDialogVisible`.
  - `dispatch`: Function to send `PermissionEvent`s to the ViewModel.

- Behavior:
  - `LaunchedEffect(permissionState.permission)` — triggers system permission dialog when state is `Requesting`.
  - `LifecycleEventEffect(ON_RESUME)` — re-checks permission when user returns from app settings.
  - Uses `rememberUpdatedState` to avoid stale state in lifecycle callbacks.

- Dialog rendering extracted to `PermissionDialogs` composable:
  - Only shown when `isDialogVisible = true`.
  - Education dialog — explains location need.
  - Denied dialog — offers rationale.
  - DeniedPermanently dialog — prompts for settings.

```kotlin
@Composable
fun HandlePermissions(
    permissions: List<String>,
    permissionState: PermissionUiState,
    dispatch: (PermissionEvent) -> Unit,
) {
    val permissionHandler = rememberPermissionHandler()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        when (permissionState.permission) {
            PermissionState.Idle -> {
                // Initial check only
                permissionHandler.resolvePermissionEvent(permissions)?.let(dispatch)
            }
            PermissionState.DeniedPermanently -> {
                // User may have enabled permission in Settings
                dispatch(PermissionEvent.ReturnedFromAppSettings)
            }
            else -> Unit  // Requesting, Denied, Education
        }
    }

    LaunchedEffect(permissionState.permission) {
        if (permissionState.permission == PermissionState.Requesting) {
            val result = permissionHandler.request(permissions)

            when (result) {
                is PermissionResult.Granted -> dispatch(PermissionEvent.AllGranted)
                is PermissionResult.Explain -> dispatch(PermissionEvent.DeniedWithRationale)
                is PermissionResult.Blocked -> dispatch(PermissionEvent.DeniedPermanently)
            }
        }
    }
}
```

### 5. Domain Models and Events

The permission domain is modeled using a set of sealed hierarchies to ensure type-safety and exhaustive handling.

- `PermissionState`: `Idle`, `Education`, `Requesting`, `Granted`, `Denied`, `DeniedPermanently`.
- `PermissionResult`: `Granted`, `Explain`, `Blocked`.
- `PermissionEvent`: `ShowEducation`, `Request`, `AllGranted`, `DeniedWithRationale`, `DeniedPermanently`, `AcceptedRationale`, `DismissedRationale`, `RequestedAppSettings`, `ReturnedFromAppSettings`, `ShowDeniedPermanentlyDialog`.
- Each event carries a reducer lambda to update `PermissionUiState` immutably.

### 6. ViewModel Integration

The ViewModel centralizes state updates and handles side effects like opening system settings.

- `dispatch(event)` updates the state using the event's reducer and handles any resulting side effects.

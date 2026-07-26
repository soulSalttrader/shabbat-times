# MVI Architecture

This project follows pure unidirectional MVI with a few naming choices that avoid Android-specific
confusion (e.g., "Event" instead of "Intent").

## MVI Terminology Mapping

The following table maps common MVI concepts to the specific terminology used in this project.

| Project Term | Common MVI Equivalent | Notes |
|-------------------|---------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Event             | Intent / Action                 | User intention or external trigger that drives state change                                                                                                                                  |
| State             | State / Model                   | Single immutable source of truth for the UI                                                                                                                                                  |
| Effect            | Effect / SideEffect / Command   | Imperative one-shot actions (network, start loop, toast)                                                                                                                                     |
| dispatch          | send / accept / dispatch        | Public entry point to send an Event into the ViewModel                                                                                                                                       |
| Reducer           | Reducer (central function)      | Pure function: (oldState, event) → newState (or sub-state)                                                                                                                                   |
| Reducible         | no direct equivalent            | Marker interface saying "this Event carries its own reducer". Also known as self-reducing events, reducer-carrying actions, or fat actions. Avoids central when switch — clean and scalable. |
| Event + Reducible | Central when (event) in reducer | preferred variant for readability and no boilerplate                                                                                                                                         |

## Why separate Events from Effects?

- `Events` are declarative: "I want to load Shabbat times".
    - They only describe intent and how the state should change.
- `Effects` are reactive:
    - Async work (network, disk, permissions) is triggered by observing state or repository flows
    - This work lives in Flows (flatMapLatest, combine, etc.), not inside reducers

## Benefits

- Unidirectional data flow ensures predictability and traceability
- Pure reducers → easy unit testing (event → state)
- Side effects are driven by state, not by imperative commands
- Async work is lifecycle-aware and cancelable by default
- No hidden behavior: everything reacts to explicit state changes
- Scales naturally as new Flows are added (data, permissions, location, analytics)

## Core MVI Concepts

The MVI architecture is built upon several core concepts that ensure a predictable data flow.

- **UiState**
- **UiEvent**
- **Reducer**  
- **Reducible&lt;S&gt;**
- **UiEffect**

## UiState

The `UiState` is a single immutable source of truth that combines all feature-specific sub-states.

- Single immutable source of truth. A data class that combines all feature-specific sub-states.

```kotlin
data class SearchUiState(
    val query: Input<String> = Input.Idle,
    val suggestionResults: SearchResultState = SearchResultState.Idle,
    val selectedSuggestion: Selection<ResolvedLocation?> = Selection.Idle,
    val visibility: SearchVisibility = SearchVisibility.Collapsed,
) : State
```

## UiEvent
- Represents user intentions or external triggers. Implemented as a sealed hierarchy.  
- Each event is **self-reducing**: it carries its own pure reducer via the `Reducible<S>` interface.

```kotlin
sealed interface SearchEvent : UiEvent, Reducible<SearchUiState> {
    data class QueryChanged(val newQuery: String) : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(
                query = Input.Value(value = newQuery),
                suggestionResults =
                    when (newQuery.trim().length >= 2) {
                        true -> SearchResultState.Loading
                        else -> SearchResultState.Idle
                    }
            )
        }
    }

    data object QueryCleared : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(
                query = Input.Idle,
                selectedSuggestion = Selection.Idle,
                suggestionResults = SearchResultState.Idle,
            )
        }
    }
    ...
}
```

## Reducer
- A pure function `(oldState: S) → newState: S` that computes the next sub-state.  
- Completely side-effect-free.

```kotlin
fun interface Reducer<S : State> {
    infix fun reduce(state: S): S

    infix fun then(next: Reducer<S>) = Reducer<S> { next.reduce(reduce(it)) }

    infix fun debug(tag: String) = takeIf { Debug.enabled }
        ?.let { baseReducer ->
            Reducer { state ->
                val nextState = reduce(state)
                val eventName = baseReducer.formatEventName()

                Log.d(tag,state.formatStateTransition(eventName = eventName, next = nextState))
                nextState
            }
        } ?: this
}
```

- A marker interface that events implement to declare:  
- *"I know how to reduce a specific sub-state of type S."*  
- This enables the clean, boilerplate-free pattern where events reduce themselves.

```kotlin
interface Reducible<S : State> {
    val reducer: Reducer<S>
}
```

## UiEffect
- Represents imperative, one-shot side effects that cannot be expressed purely (e.g., starting
background loops, network requests, showing toasts, navigation, logging).

```kotlin
sealed interface UiEffect {
    data class ShowToast(val message: UiText) : UiEffect

    data class ShowSnackBar(
        val message: UiText,
        val actionLabel: UiText? = null,
        val onAction: (() -> Unit)? = null,
    ) : UiEffect

    data object OpenAppSettings : UiEffect
}
```

## Data Flow

The flow of data through the system is entirely unidirectional, moving from user interaction to state update.

### Pure MVI Cycle

The following diagram illustrates the reactive flow from data sources to UI state.

```kotlin
savedLocationsRepository.locations (includes GPS entry with SavedLocation.GPS_ID)
currentLocationRepository.location (in-memory, resets on restart)
→ state in GpsViewModel resolves GPS via ResolveGpsLocationUseCase
→ updateCurrentLocationUseCase saves result to both currentLocationRepository and savedLocationsRepository
→ halachicTimesFlow fetches times per Coordinates
→ combine merges savedLocations + currentLocation + halachicTimes + permissionState
→ reducer maps to ShabbatEvent.ShabbatEntryLoaded
→ computes LocationStatus per entry (Current/LastKnownLocation/Nearby/Locating/NoPermission/Unknown)
→ order determined by sortOrder from savedLocationsRepository (Room)
→ produces ShabbatUiState with ImmutableList
→ UI observes StateFlow → renders automatically
```

### Data sources in combine

The `ShabbatViewModel` combines multiple reactive flows to derive the final UI state.

| Flow | Source | Written by |
|------|------------------------|---------------------------------------------------|
| `savedLocationsRepository.locations` | Room                   | `SaveLocationUseCase`, `RemoveLocationUseCase`    |
| `currentLocationRepository.location` | In-memory shared state | `UpdateCurrentLocationUseCase` via `GpsViewModel` |
| `halachicTimesFlow` | Model, solar API       | `GetHalachicTimesUseCase`                         |
| `permissionRepository.permissionState` | In-memory shared state | `PermissionViewModel`                             |

### Step-by-Step

The following steps detail the lifecycle of a user interaction within the MVI cycle.

1. **User Interaction**
  - UI calls `viewModel.dispatch(event)` (e.g. `GpsLocationRequested`, `LocationDeleted`)

2. **Pure State Reduction**
  - The event's reducer produces a new immutable `UiState`
  - No direct state mutation — reducers are pure functions

3. **Reactive Triggers**
  - `savedLocationsRepository.locations` — emits on every save/remove
  - `GpsViewModel.state` — emits `GpsUiState` when GPS resolves via `ResolveGpsLocationUseCase`
  - `permissionRepository.permissionState` — emits on permission changes
  - All combined via `combine` + `flatMapLatest`

4. **Asynchronous Work**
  - `halachicTimesFlow` observes all locations (saved + GPS)
  - For each new list, `GetHalachicTimesUseCase` fetches solar times per `Coordinates`
  - Times domain has zero knowledge of cities — only `Coordinates` + `ZoneId`
  - Partial failures show toast, full failures dispatch `ShabbatEntryLoadFailed`

5. **State Feedback**
  - Results transformed into Events (`ShabbatEntryLoaded`)
  - Reducers compute `LocationStatus` (Current/LastKnownLocation/Nearby/Locating/NoPermission/Unknown) dynamically
  - `ShabbatEntry` built by matching times to locations via normalized coordinates

6. **UI Update**
  - Compose observes `ShabbatUiState` — single source of truth
  - `ImmutableList` + `@Immutable` annotations ensure correct recomposition

  ```kotlin
  fun dispatch(event: AppEvent) {
      _state.updateAndGet { current ->
          when (event) {
              is ShabbatEvent -> event.reducer reduce current
              else            -> current
          }
      }

      when (event) {
          is ShabbatEvent.LocationDeleted  -> handleDeleteLocation(event)
          is ShabbatEvent.ReorderLocations -> handleReorderLocations(event)
          else                             -> Unit
      }
  }
  ```

### In Plain English

For a high-level understanding, the following summary describes the data flow in simple terms.

- The user does something → an Event is created and sent to the ViewModel (e.g. `GpsLocationRequested`, `LocationDeleted`, `SuggestionSelected`)
- The Event knows exactly how to calculate the new State (pure, no side effects).
- The ViewModel updates the State → the UI refreshes automatically.
- If something "real" needs to happen (fetch times, save location, show a toast), the ViewModel handles it reactively via Flows or sends an Effect.
- Flows (`halachicTimesFlow`, `currentLocationRepository.location`) observe data sources and feed into `combine` — state is always derived, never manually assembled.
- ViewModels communicate via shared repositories, not directly: `GpsViewModel` resolves GPS → writes to `CurrentLocationRepository` → `ShabbatViewModel` reads and reacts automatically.
- Same pattern for permissions: `PermissionViewModel` writes to `PermissionRepository` →`ShabbatViewModel` reads and computes `LocationStatus` per entry.
- Results flow back as new Events (`ShabbatEventLoaded`), keeping everything in one direction.
- This unidirectional, pure MVI flow ensures predictability, testability,
  and easy reasoning about application behavior.

### Example: Shabbat times loading

The loading of Shabbat times is a complex process that demonstrates the power of the reactive MVI flow.

1. `ShabbatViewModel` observes four data sources via `combine`:
  - `savedLocationsRepository.locations` — user-saved locations
  - `currentLocationRepository.location` — GPS-resolved location (written by `GpsViewModel`)
  - `halachicTimesFlow` — fetched times per `Coordinates`
  - `permissionRepository.permissionState` — for `LocationStatus` computation

2. `halachicTimesFlow` reacts to any change in locations (saved or GPS):
  - Combines saved locations + current GPS location into one list
  - Calls `GetHalachicTimesUseCase(allLocations)` — fetches solar times per `Coordinates`
  - Partial failures show a toast, full failure dispatches `ShabbatEntryLoadFailed`
  - Emits `List<HalachicTimes>` with raw `LocalTime` values

3. `ShabbatEntryLoaded` reducer:
  - Matches times to locations via normalized `Coordinates`
  - Computes `LocationStatus` per entry from permission + GPS distance:
    - `NoPermission` — permission denied
    - `Locating` — permission requesting
    - `Current` — distance < 0.1km
    - `Nearby(distanceKm)` — distance ≥ 0.1km
    - `Unknown` — no GPS available
  - Builds `ImmutableList<ShabbatEntry>` for UI

4. `ShabbatResultState` transitions:
  - `Empty` — no saved locations and no GPS location
  - `Loading` — locations exist but times not yet fetched (per-card spinner)
  - `Ready(entries)` — locations and times both present
  - `Failure` — unrecoverable error

5. Empty card (no locations yet) — `LocationStatus` derived from `GpsUiState.gpsResult`:
  - `Idle` → `Unknown` → "Unknown distance"
  - `Loading` → `Locating` → "Getting your location..."
  - `Resolved` → `Current` → "Your current location"

6. UI observes single `ShabbatUiState` — cards render immediately with spinner,
   times fill in per-card when fetched

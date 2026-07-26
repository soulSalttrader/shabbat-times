# Search Architecture

The search functionality is designed as a reactive, modular system that bridges user input with the core calculation engine. It demonstrates a clean separation between generic UI components, domain-specific state wrappers, and optimized data fetching.

## Key Features

The search architecture provides a robust set of features designed for a seamless user experience.

- **Atomic UI Design**: A three-tier hierarchy (Generic `SearchBarInputField` → Domain-Specific `CitySearchBarInputField` → `CitySearchScreen`) that ensures UI components remain stateless, reusable, and easy to test.
- **Self-Reducing MVI**: Search events implement `Reducible<SearchUiState>`, moving business logic into discrete, testable units (`SearchReducer`) and keeping the ViewModel as a clean orchestrator.
- **Robust State Wrappers**: Replaces primitive strings with `Input<T>` and `Selection<T>` types to explicitly model field states (Idle, Value, Loading, Empty), eliminating "null-checking hell" in the UI.
- **Reactive Data Pipeline**: Utilizes a reactive bridge where a city selection in the search module automatically triggers a data re-fetch in the halachic module via `flatMapLatest`.
- **Performance Optimized**: Features built-in 300ms debouncing, minimum query thresholds (2+ chars), and automatic geo-localization based on the user's system locale.
- **Smart UX Logic**: Encapsulates complex interactions, such as the dual-purpose trailing icon (Clear vs. Collapse), into isolated helper functions to maintain UI readability.

## Key Components

The following UI components and helper functions form the foundation of the search interface.

| Component                     | Role                                                                                                                                                            | Type       |
|-------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|------------|
| `SearchBarInputField`         | A generic, private composable that wraps SearchBarDefaults.InputField and provides a standard setup for text state, expansion, and search actions.              | Composable | 
| `LocationSearchBarInputField` | A specialized, public composable that configures SearchBarInputField with specific defaults for city searching (placeholder text, icons, etc.).                 | Composable | 
| `LocationSearchScreen`        | The main screen composable that orchestrates the search UI, manages state, and handles user events.                                                             | Composable |
| `onTrailingIconClick`         | A private helper function that determines the action for the trailing icon: clear the query if it exists, or toggle the search bar's expansion state otherwise. | Function   |

## High-Level Flow

The search process follows a reactive unidirectional flow, ensuring data consistency and smooth UI updates.

In brief:
- User types → debounced query → `GeocodingRepository.autocomplete()` → suggestions
- User taps card → GPS resolves → `CurrentLocationRepository` + `SavedLocationsRepository` updated
- `ShabbatViewModel` reacts to repository changes automatically

## Search Components

The UI is built using a structured hierarchy of specialized search components.

### FabMenu / FabItem / FabAction

Models FAB (Floating Action Button) entries as data, separate from navigation.
A FAB triggers an in-place UI action (e.g. toggling an overlay) — it does not
change the back stack or navigate to a destination.

#### Components

- **`FabItem`** — plain data describing one FAB entry: title, icons, and a
  `FabAction`. No lambdas, no captured state — fully testable/comparable.
- **`FabAction`** — sealed interface enumerating what a FAB tap *means*
  (e.g. `ToggleSearchOverlay`). Adding a new case forces every consuming
  `when` to handle it (no `else` branch used).
- **`FabItems`** — object holding concrete `FabItem` instances (e.g.
  `FabItems.Search`), analogous to `NavItems`.
- **`FabMenu`** — stateless composable that renders a `List<FabItem>` and
  forwards taps via a single callback: `onAction: (FabAction) -> Unit`. It
  has no knowledge of what any action *does* — it just reports which item's
  action was tapped.

```kotlin
sealed interface FabAction {
    data object ToggleSearchOverlay : FabAction
}

data class FabItem(
    val title: UiText,
    val selectedIcon: UiIcon,
    val unselectedIcon: UiIcon,
    val action: FabAction,
)

object FabItems {
    val Search = FabItem(
        title = UiText.Resource(R.string.search_new_location),
        selectedIcon = UiIcon.Resource(R.drawable.add_outlined_24),
        unselectedIcon = UiIcon.Resource(R.drawable.add_outlined_24),
        action = FabAction.ToggleSearchOverlay,
    )
}

fun FabMenu(
    onAction: (FabAction) -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean? = null,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    items: List<FabItem> = listOf(FabItems.Search),
)
```

Call site interprets the action

```kotlin
FabMenu(
    items = fabItems,
    onAction = { action ->
        when (action) {
            FabAction.ToggleSearchOverlay ->
                searchConfig.action.onChangeVisibility(!searchConfig.state.searchActive)
        }
    },
)
```

#### FabItem vs NavItem

Both pair a UI entry (title + icons) with a sealed "what happens" value, but they intentionally live in separate systems

| | `NavItem` | `FabItem` |
|---|---|---|
| Paired with | `NavTarget` + `NavRole` | `FabAction` |
| Meaning | Navigates to a destination (changes back stack) | Triggers a local, in-place UI action |
| Interpreted by | `Navigator` | Whatever owns the relevant local state (e.g. `searchConfig`) |

### SearchBarInputField

A foundational, private composable that abstracts Material 3's SearchBarDefaults.InputField into a standardized, reusable component.

- Purpose: Encapsulates boilerplate configurations (shape, padding, text-field state logic) to ensure UI consistency across different search contexts while hiding implementation details.
- Contract: Exposes a clean API for state management and functional callbacks (onSearch, onExpandedChange).

### LocationSearchBarInputField

A domain-specific specialization of SearchBarInputField pre-configured for geographic lookups.

- Purpose: Provides a specialized search entry point with pre-defined city-related icons and localized strings.
- Smart Logic: Integrates the onTrailingIconClick helper to handle the contextual transition between "clearing text" and "collapsing the UI."

```kotlin
@Composable
fun LocationSearchBarInputField(
    state: TextFieldState,
    hasQuery: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    expanded: Boolean,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    // ...
    trailingIcon: @Composable (() -> Unit)? = {
        TrailingSearchIconButton(
            onClick = onTrailingIconClick(hasQuery, onClear, onExpandedChange, expanded)
        )
    },
) {
    SearchBarInputField(
        state = state,
        expanded = expanded,
        onSearch = onSearch,
        onExpandedChange = onExpandedChange,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
    )
}
```

### LocationSearchScreen

The top-level screen container that bridges the MVI state with the UI components.

- Role: A stateless orchestrator that observes the SearchUiState and maps UI callbacks to the dispatch function.
- Composition: Manages the vertical layout between the input field and the CitySearchSuggestionPanel, ensuring smooth transitions between expanded and collapsed states.

```kotlin
@Composable
fun LocationSearchScreen(
    searchConfig: SearchConfig,
    modifier: Modifier = Modifier,
) {
    //...

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            LocationSearchBarInputField(
                state = state,
                hasQuery = searchConfig.state.hasQuery,
                expanded = searchConfig.state.searchActive,
                onExpandedChange = { expanded -> searchConfig.action.onChangeVisibility(!expanded) },
                onSearch = { query ->
                    searchConfig.action.onQueryChanged(query)
                    searchConfig.action.onSearchCommitted()
                },
                onClear = {
                    searchConfig.action.onQueryCleared()
                    state.clearText()
                },
            )

            LocationSearchSuggestionPanel(
                query = state.text.toString(),
                expanded = searchConfig.state.searchActive,
                suggestions = searchConfig.state.suggestions,
                onSuggestionSelected = { suggestion ->
                    searchConfig.action.onSuggestionSelected(suggestion)
                    state.setTextAndPlaceCursorAtEnd(suggestion.name)
                },
            )
        }
    }
}
```

## SearchViewModel

The `SearchViewModel` manages text-based autocomplete search. GPS reverse geocoding and writing to `CurrentLocationRepository` are handled separately by the `GpsViewModel`.

### Key Responsibilities

The `SearchViewModel` is responsible for coordinating the data streams and user interactions related to text-based location searching.

- **Autocomplete Search:** Reactive pipeline (`queryFlow` → debounce 300ms → `flatMapLatest`) transforms user input into `List<ResolvedLocation>` suggestions via `GeocodingRepository.autocomplete()`
- **Reactive State Exposure:** Exposes `SearchUiState` via `StateFlow` — combines base state + `suggestionResults`
- **Unidirectional Event Dispatch:** Single `dispatch(AppEvent)` entry point with self-reducing events — each `SearchEvent` contains its own reducer
- **Lifecycle-Aware Streaming:** `stateIn(viewModelScope, WhileSubscribed(5000))` — flows only active while UI is subscribed
- **Side-Effect Management:** One-shot actions (toasts, navigation) via `SharedFlow<AppEffect>`, separate from state updates

### Key Components

The following components and internal processes drive the `SearchViewModel`'s functionality.

| Component | Role | Type |
|-----------|------|------|
| `SearchUiState` | A data class that represents the entire state of the search screen at any given moment, including the query, search results, loading status, and expanded state. | Data Class |
| `SearchEvent` | A sealed interface defining all possible user actions that can be dispatched from the UI to the ViewModel (e.g., QueryChanged, SearchTriggered, Clear, ...). | Sealed Interface |
| `searchDispatch` | The single public function on the ViewModel that the UI calls to send SearchEvents for processing. | Function | 
| `debounce` | A Flow operator used within the ViewModel to delay processing of the QueryChanged event, preventing a network request for every keystroke. | Coroutine Flow |

### SearchUiState

The `SearchUiState` provides a single, immutable source of truth for the search feature.

```kotlin
data class SearchUiState(
    val query: Input<String> = Input.Idle,
    val suggestionResults: SearchResultState = SearchResultState.Idle,
    val selectedSuggestion: Selection<ResolvedLocation?> = Selection.Idle,
    val visibility: SearchVisibility = SearchVisibility.Collapsed,
) : State
```

## Detailed Data Flow

The following sections describe the specific data flows for different search operations.

### Autocomplete

The autocomplete flow handles real-time address suggestions as the user types.

1. **State Observation:** `ShabbatScreen` observes `SearchUiState` — combined result of local UI state, suggestions stream and GPS result.
2. **Event Dispatch:** User types → `SearchEvent.QueryChanged` dispatched → reducer immediately updates `Input` state and `SearchResultState.Loading`.
3. **Reactive Query Stream:** `queryFlow` extracts normalized string via `.map { it.query.normalizedOrEmpty() }.distinctUntilChanged()` — ignores redundant updates.
4. **Debounced Transformation:** `locationSuggestionsFlow` listens to `queryFlow` with 300ms debounce — prevents API spam.
5. **Reactive Fetching:** `flatMapLatest` → `GeocodingRepository.autocomplete(query)`. Queries shorter than 2 characters short-circuit to `Empty`.
6. **Resilient Execution:**
  - Success → `SearchResultState.Suggestions(locations)`
  - No results → `SearchResultState.Empty`
  - Failure → `SearchResultState.Failure` + `AppEffect.ShowToast`
7. **State Synthesis:** `combine(_state, locationSuggestionsFlow)` → `SearchEvent.SuggestionsLoaded` reducer → `suggestionResults` updated.
8. **UI Recomposition:** `LocationSearchSuggestionPanel` recomposes automatically.
9. **Selection:** User taps suggestion → `SearchEvent.SuggestionSelected` → `SaveLocationUseCase(resolved)` → `SavedLocationsRepository.save()` → `savedLocationsRepository.locations` emits → `ShabbatViewModel` reacts → new card appears.

### Reverse Geocoding

The reverse geocoding flow resolves the user's physical coordinates into a meaningful location. This logic is encapsulated in `GpsViewModel`.

1. **User taps card** → `GpsEvent.GpsLocationRequested` dispatched to `GpsViewModel` → `gpsResult = GpsResultState.Loading`
2. **`GpsViewModel.state`** observes `ResolveGpsLocationUseCase()` — permission-gated GPS + reverse geocode
3. **GPS resolves** → `ResolvedLocation` emitted via `onEach` in `GpsViewModel`:
  - `UpdateCurrentLocationUseCase(resolved)` → `CurrentLocationRepository.update()` → `ShabbatViewModel` reacts
  - `SaveLocationUseCase(resolved)` → `SavedLocationsRepository.save()` → new card appears
  - `gpsResult = GpsResultState.Resolved(resolved)`
4. **Failure** → `GpsResultState.Failure` + `AppEffect.ShowToast`
5. **`ShabbatScreen` empty card** maps `gpsResult` → `LocationStatus` → label updates reactively

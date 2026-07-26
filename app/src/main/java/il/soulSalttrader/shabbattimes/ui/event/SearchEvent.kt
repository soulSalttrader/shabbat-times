package il.soulSalttrader.shabbattimes.ui.event

import android.util.Log
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.ui.Input
import il.soulSalttrader.shabbattimes.ui.Selection
import il.soulSalttrader.shabbattimes.ui.reducer.Reducible
import il.soulSalttrader.shabbattimes.ui.reducer.SearchReducer
import il.soulSalttrader.shabbattimes.ui.search.SearchResultState
import il.soulSalttrader.shabbattimes.ui.search.SearchUiState
import il.soulSalttrader.shabbattimes.ui.search.SearchVisibility

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

    data class SuggestionsLoaded(val resolvedLocations: List<ResolvedLocation>) : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(
                suggestionResults = when {
                    state.query is Input.Idle   -> SearchResultState.Idle
                    state.query is Input.Empty  -> SearchResultState.Idle
                    resolvedLocations.isEmpty() -> SearchResultState.Empty
                    else                        -> SearchResultState.Suggestions(resolvedLocations)
                }
            )
        }
    }

    class SuggestionsLoadFailed(val cause: Throwable?) : SearchEvent {
        override val reducer = SearchReducer { state ->
            if (Debug.enabled) Log.d("ShabbatEvent", "cause: $cause")
            state.copy(suggestionResults = SearchResultState.Failure(cause))
        }
    }

    data class SuggestionSelected(val resolvedLocation: ResolvedLocation) : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(
                query = Input.Value(value = resolvedLocation.name),
                selectedSuggestion = Selection.Selected(value = resolvedLocation),
            )
        }
    }

    data class SearchVisibilityChanged(val expanded: Boolean) : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(
                visibility = when (expanded) {
                    true -> SearchVisibility.Expanded
                    else -> SearchVisibility.Collapsed
                }
            )
        }
    }

    object SearchCommitted : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(visibility = SearchVisibility.Collapsed)
        }
    }
}
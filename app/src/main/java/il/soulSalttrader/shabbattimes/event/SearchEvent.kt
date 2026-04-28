package il.soulSalttrader.shabbattimes.event

import android.util.Log
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.content.Input
import il.soulSalttrader.shabbattimes.content.Selection
import il.soulSalttrader.shabbattimes.content.search.SearchMode
import il.soulSalttrader.shabbattimes.content.search.SearchResultState
import il.soulSalttrader.shabbattimes.content.search.SearchUiState
import il.soulSalttrader.shabbattimes.content.search.SearchVisibility
import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.reducer.Reducible
import il.soulSalttrader.shabbattimes.reducer.SearchReducer

sealed interface SearchEvent : AppEvent, Reducible<SearchUiState> {
    data class QueryChanged(val newQuery: String) : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(
                query = Input.Value(value = newQuery),
                resultState =
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
                resultState = SearchResultState.Idle,
            )
        }
    }

    data object LoadSuggestions : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(resultState = SearchResultState.Loading)
        }
    }

    data class SuggestionsLoaded(val resolvedLocations: List<ResolvedLocation>) : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(
                resultState = when {
                    state.query is Input.Idle   -> SearchResultState.Idle
                    state.query is Input.Empty  -> SearchResultState.Idle
                    resolvedLocations.isEmpty() -> SearchResultState.NoResults
                    else                        -> SearchResultState.Results(resolvedLocations)
                }
            )
        }
    }

    class SuggestionsLoadFailed(val message: String, val cause: Throwable?) : SearchEvent {
        override val reducer = SearchReducer { state ->
            if (Debug.enabled) Log.d("ShabbatEvent", "message: $message, cause: $cause")
            state.copy(resultState = SearchResultState.Failure(message, cause))
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

    data class SearchModeChanged(val mode: SearchMode) : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(
                searchMode = mode,
                resultState = SearchResultState.Idle
            )
        }
    }

    data object RetrySearch : SearchEvent {
        override val reducer = SearchReducer { state -> state }
    }

    object SearchCommitted : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(visibility = SearchVisibility.Collapsed)
        }
    }
}
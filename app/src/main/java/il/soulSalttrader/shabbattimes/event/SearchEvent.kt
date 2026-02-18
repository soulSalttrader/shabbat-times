package il.soulSalttrader.shabbattimes.event

import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.model.SearchMode
import il.soulSalttrader.shabbattimes.model.SearchState
import il.soulSalttrader.shabbattimes.model.SearchUiState
import il.soulSalttrader.shabbattimes.reducer.Reducible
import il.soulSalttrader.shabbattimes.reducer.SearchReducer

sealed interface SearchEvent : AppEvent, Reducible<SearchUiState> {
    data class QueryChanged(val newQuery: String) : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(
                query = newQuery,
                searchState =
                    when (newQuery.trim().length >= 2) {
                        true -> SearchState.Loading
                        else -> SearchState.Idle
                    }
            )
        }
    }

    data object QueryCleared : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(
                query = "",
                selectedSuggestion = null,
                searchState = SearchState.Idle,
            )
        }
    }

    data class SuggestionsLoaded(val cities: List<City>) : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(
                searchState = when {
                    state.query.isBlank() -> SearchState.Idle
                    cities.isEmpty()      -> SearchState.NoResults
                    else                  -> SearchState.Results(cities)
                }
            )
        }
    }

    data class SuggestionSelected(val city: City) : SearchEvent {
        override val reducer = SearchReducer { state -> 
            state.copy(
                query = city.name,
                selectedSuggestion = city,
                searchState = SearchState.Idle,
            )
        }
    }

    data class SearchVisibilityChanged(val expanded: Boolean) : SearchEvent {
        override val reducer = SearchReducer { state ->
            if (expanded) state
            else state.copy(
                searchState = SearchState.Idle
            )
        }
    }

    data class SearchModeChanged(val mode: SearchMode) : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(
                searchMode = mode,
                searchState = SearchState.Idle
            )
        }
    }

    data object RetrySearch : SearchEvent {
        override val reducer = SearchReducer { state -> state }
    }
}
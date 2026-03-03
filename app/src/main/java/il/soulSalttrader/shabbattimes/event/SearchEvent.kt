package il.soulSalttrader.shabbattimes.event

import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.content.search.SearchMode
import il.soulSalttrader.shabbattimes.content.search.SearchResultState
import il.soulSalttrader.shabbattimes.content.search.SearchUiState
import il.soulSalttrader.shabbattimes.content.search.SearchVisibility
import il.soulSalttrader.shabbattimes.reducer.Reducible
import il.soulSalttrader.shabbattimes.reducer.SearchReducer

sealed interface SearchEvent : AppEvent, Reducible<SearchUiState> {
    data class QueryChanged(val newQuery: String) : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(
                query = newQuery,
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
                query = "",
                selectedSuggestion = null,
                resultState = SearchResultState.Idle,
            )
        }
    }

    data class SuggestionsLoaded(val cities: List<City>) : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(
                resultState = when {
                    state.query.isBlank() -> SearchResultState.Idle
                    cities.isEmpty()      -> SearchResultState.NoResults
                    else                  -> SearchResultState.Results(cities)
                }
            )
        }
    }

    data class SuggestionSelected(val city: City) : SearchEvent {
        override val reducer = SearchReducer { state -> 
            state.copy(
                query = city.name,
                selectedSuggestion = city,
            )
        }
    }

    data class SearchVisibilityChanged(val expanded: Boolean) : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(
                visibility = when(expanded) {
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
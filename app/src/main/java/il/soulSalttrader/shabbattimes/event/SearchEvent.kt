package il.soulSalttrader.shabbattimes.event

import android.util.Log
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.content.Input
import il.soulSalttrader.shabbattimes.content.Selection
import il.soulSalttrader.shabbattimes.content.search.SearchResultState
import il.soulSalttrader.shabbattimes.content.search.SearchUiState
import il.soulSalttrader.shabbattimes.content.search.SearchVisibility
import il.soulSalttrader.shabbattimes.location.LocationPermission
import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.reducer.Reducible
import il.soulSalttrader.shabbattimes.reducer.SearchReducer

sealed interface SearchEvent : AppEvent, Reducible<SearchUiState> {
    data class QueryChanged(val newQuery: String) : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(
                query = Input.Value(value = newQuery),
                suggestionResults =
                    when (newQuery.trim().length >= 2) {
                        true -> SearchResultState.Requesting
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
                    resolvedLocations.isEmpty() -> SearchResultState.NoResults
                    else                        -> SearchResultState.Suggestions(resolvedLocations)
                }
            )
        }
    }

    class SuggestionsLoadFailed(val message: String, val cause: Throwable?) : SearchEvent {
        override val reducer = SearchReducer { state ->
            if (Debug.enabled) Log.d("ShabbatEvent", "message: $message, cause: $cause")
            state.copy(suggestionResults = SearchResultState.Failure(message, cause))
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

    data class GpsLocationLoaded(val location: ResolvedLocation) : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(gpsResult = SearchResultState.GpsLocation(location))
        }
    }

    data class GpsLocationError(val message: String) : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(gpsResult = SearchResultState.Failure(message))
        }
    }

    data object GpsLocationRequested : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(gpsResult = SearchResultState.Requesting)
        }
    }

    data class GpsPermissionChanged(val permission: LocationPermission) : SearchEvent {
        override val reducer = SearchReducer { state ->
            state.copy(
                gpsResult = when (permission) {
                    is LocationPermission.Idle -> SearchResultState.Idle
                    is LocationPermission.Requesting -> SearchResultState.Requesting
                    is LocationPermission.Denied -> SearchResultState.NoPermission
                    is LocationPermission.DeniedPermanently -> SearchResultState.NoPermission

                    else -> state.gpsResult
                }
            )
        }
    }
}
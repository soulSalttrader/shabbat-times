package il.soulSalttrader.shabbattimes.ui.search

import il.soulSalttrader.shabbattimes.model.ResolvedLocation

sealed interface SearchResultState {
    data object Idle : SearchResultState
    data object Loading : SearchResultState
    data object Empty : SearchResultState
    data class Suggestions(val suggestions: List<ResolvedLocation>) : SearchResultState
    data class GpsResolved(val location: ResolvedLocation) : SearchResultState
    data class Failure(val cause: Throwable? = null) : SearchResultState
}
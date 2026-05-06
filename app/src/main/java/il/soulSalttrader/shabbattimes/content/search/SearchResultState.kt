package il.soulSalttrader.shabbattimes.content.search

import il.soulSalttrader.shabbattimes.model.ResolvedLocation

sealed interface SearchResultState {
    data object Idle : SearchResultState
    data object Requesting : SearchResultState
    data object NoResults : SearchResultState
    data object NoPermission : SearchResultState
    data class Suggestions(val suggestions: List<ResolvedLocation>) : SearchResultState
    data class GpsLocation(val location: ResolvedLocation) : SearchResultState
    data class Failure(val message: String, val cause: Throwable? = null) : SearchResultState
}
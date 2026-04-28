package il.soulSalttrader.shabbattimes.content.search

import il.soulSalttrader.shabbattimes.model.ResolvedLocation

sealed interface SearchResultState {
    data object Idle : SearchResultState
    data object Loading : SearchResultState
    data object NoResults : SearchResultState
    data class Results(val suggestions: List<ResolvedLocation>) : SearchResultState
    data class Failure(val message: String, val cause: Throwable? = null) : SearchResultState
}
package il.soulSalttrader.shabbattimes.content.search

import il.soulSalttrader.shabbattimes.model.City

sealed interface SearchResultState {
    object Idle : SearchResultState
    object Loading : SearchResultState
    object NoResults : SearchResultState
    data class Results(val cities: List<City>) : SearchResultState
    data class Failure(val message: String, val cause: Throwable? = null) : SearchResultState
}
package il.soulSalttrader.shabbattimes.model

sealed interface SearchResultState {
    object Idle : SearchResultState
    object Loading : SearchResultState
    object NoResults : SearchResultState
    data class Results(val cities: List<City>) : SearchResultState
    data class Failure(val message: String, val cause: Throwable? = null) : SearchResultState
}
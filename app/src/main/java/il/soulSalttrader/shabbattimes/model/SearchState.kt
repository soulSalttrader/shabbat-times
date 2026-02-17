package il.soulSalttrader.shabbattimes.model

sealed interface SearchState {
    object Idle : SearchState
    object Loading : SearchState
    object NoResults : SearchState
    data class Results(val cities: List<City>) : SearchState
    data class Failure(val message: String, val cause: Throwable? = null) : SearchState
}
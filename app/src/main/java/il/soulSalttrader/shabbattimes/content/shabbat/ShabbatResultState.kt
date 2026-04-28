package il.soulSalttrader.shabbattimes.content.shabbat

import il.soulSalttrader.shabbattimes.model.LocationWithTimes
import il.soulSalttrader.shabbattimes.model.State

sealed interface ShabbatResultState : State {
    data object Idle : ShabbatResultState
    data object Loading : ShabbatResultState
    data object NoResults : ShabbatResultState
    data class Results(val data: List<LocationWithTimes>) : ShabbatResultState
    data class Failure(val message: String, val cause: Throwable? = null) : ShabbatResultState
}
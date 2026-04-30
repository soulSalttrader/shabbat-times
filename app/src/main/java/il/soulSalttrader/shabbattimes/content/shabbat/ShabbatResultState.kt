package il.soulSalttrader.shabbattimes.content.shabbat

import androidx.compose.runtime.Immutable
import il.soulSalttrader.shabbattimes.model.LocationWithTimes
import il.soulSalttrader.shabbattimes.model.State

@Immutable
sealed interface ShabbatResultState : State {
    @Immutable
    data object Idle : ShabbatResultState
    @Immutable
    data object Loading : ShabbatResultState
    @Immutable
    data object NoResults : ShabbatResultState
    @Immutable
    data class Results(val data: List<LocationWithTimes>) : ShabbatResultState
    @Immutable
    data class Failure(val message: String, val cause: Throwable? = null) : ShabbatResultState
}
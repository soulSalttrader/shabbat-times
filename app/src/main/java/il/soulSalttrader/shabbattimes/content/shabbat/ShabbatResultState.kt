package il.soulSalttrader.shabbattimes.content.shabbat

import androidx.compose.runtime.Immutable
import il.soulSalttrader.shabbattimes.model.LocationWithTimes
import il.soulSalttrader.shabbattimes.model.State
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface ShabbatResultState : State {
    @Immutable
    data object Idle : ShabbatResultState
    @Immutable
    data object Loading : ShabbatResultState
    @Immutable
    data object Empty : ShabbatResultState
    @Immutable
    data class Ready(val entries: ImmutableList<LocationWithTimes>) : ShabbatResultState
    @Immutable
    data class Failure(val message: String, val cause: Throwable? = null) : ShabbatResultState
}
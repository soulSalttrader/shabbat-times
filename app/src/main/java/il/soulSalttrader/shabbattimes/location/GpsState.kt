package il.soulSalttrader.shabbattimes.location

import androidx.compose.runtime.Immutable
import il.soulSalttrader.shabbattimes.model.State

@Immutable
sealed interface GpsState : State {
    @Immutable
    data object Idle : GpsState
    @Immutable
    data object Loading : GpsState
    @Immutable
    data object NoPermission : GpsState
    @Immutable
    data object Ready : GpsState
    @Immutable
    data class Error(val message: String, val cause: Throwable? = null) : GpsState
}
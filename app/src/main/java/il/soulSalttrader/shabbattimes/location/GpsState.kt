package il.soulSalttrader.shabbattimes.location

import il.soulSalttrader.shabbattimes.model.State

sealed interface GpsState : State {
    data object Idle : GpsState
    data object Loading : GpsState
    data object NoPermission : GpsState
    data object Ready : GpsState
    data class Error(val message: String, val cause: Throwable? = null) : GpsState
}
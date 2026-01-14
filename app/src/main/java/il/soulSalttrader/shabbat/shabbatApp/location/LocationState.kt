package il.soulSalttrader.retro.shabbatApp.location

import il.soulSalttrader.retro.core.model.State

sealed interface LocationState : State {
    data object Idle : LocationState
    data object Loading : LocationState

    data class Available(val location: LocationData) : LocationState
    data class Unavailable(val message: String, val cause: Throwable? = null) : LocationState
}
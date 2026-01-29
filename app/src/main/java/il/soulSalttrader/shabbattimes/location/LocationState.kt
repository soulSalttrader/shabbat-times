package il.soulSalttrader.shabbattimes.location

import il.soulSalttrader.shabbattimes.model.State

sealed interface LocationState : State {
    data object Idle : LocationState
    data object Loading : LocationState

    data class Available(val location: LocationData) : LocationState
    data class Unavailable(val message: String, val cause: Throwable? = null) : LocationState
}
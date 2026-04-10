package il.soulSalttrader.shabbattimes.location

import android.location.Location
import il.soulSalttrader.shabbattimes.model.State

sealed interface LocationState : State {
    data object Idle : LocationState
    data object Loading : LocationState
    data object NoResult : LocationState
    data class Result(val location: Location) : LocationState
    data class Unavailable(val message: String, val cause: Throwable? = null) : LocationState
}
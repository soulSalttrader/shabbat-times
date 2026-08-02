package il.soulSalttrader.shabbattimes.ui.gps

import il.soulSalttrader.shabbattimes.model.ResolvedLocation

sealed interface GpsResultState {
    data object Idle : GpsResultState
    data object Loading : GpsResultState
    data class Resolved(val location: ResolvedLocation) : GpsResultState
    data class Failure(val cause: Throwable? = null) : GpsResultState
}

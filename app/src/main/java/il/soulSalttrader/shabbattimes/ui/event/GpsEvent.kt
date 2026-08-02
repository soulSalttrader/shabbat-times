package il.soulSalttrader.shabbattimes.ui.event

import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.ui.gps.GpsResultState
import il.soulSalttrader.shabbattimes.ui.gps.GpsUiState
import il.soulSalttrader.shabbattimes.ui.reducer.Reducer
import il.soulSalttrader.shabbattimes.ui.reducer.Reducible

sealed interface GpsEvent : UiEvent, Reducible<GpsUiState> {
    data class GpsLocationLoaded(val location: ResolvedLocation?) : GpsEvent {
        override val reducer = Reducer<GpsUiState> { state ->
            state.copy(
                gpsResult = when (location) {
                    null -> GpsResultState.Idle
                    else -> GpsResultState.Resolved(location)
                },
            )
        }
    }

    data class GpsLocationError(val cause: Throwable) : GpsEvent {
        override val reducer = Reducer<GpsUiState> { state ->
            state.copy(gpsResult = GpsResultState.Failure(cause))
        }
    }

    data object GpsLocationRequested : GpsEvent {
        override val reducer = Reducer<GpsUiState> { state ->
            state.copy(gpsResult = GpsResultState.Loading)
        }
    }
}

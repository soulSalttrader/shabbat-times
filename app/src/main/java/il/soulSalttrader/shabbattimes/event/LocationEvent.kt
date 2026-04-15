package il.soulSalttrader.shabbattimes.event

import android.location.Location
import il.soulSalttrader.shabbattimes.location.LocationState
import il.soulSalttrader.shabbattimes.location.LocationStatus
import il.soulSalttrader.shabbattimes.location.LocationUiState
import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.reducer.LocationReducer
import il.soulSalttrader.shabbattimes.reducer.Reducible

sealed interface LocationEvent : AppEvent, Reducible<LocationUiState> {
    data object LocationRequested : LocationEvent {
        override val reducer = LocationReducer { state ->
            state.copy(location = LocationState.Loading)
        }
    }

    data class LocationLoaded(val location: Location) : LocationEvent {
        override val reducer = LocationReducer { state ->
            state.copy(
                location = LocationState.Result(location),
                status = LocationStatus.Current,
            )
        }
    }

    data object CurrentLocationRemoved : LocationEvent {
        override val reducer = LocationReducer { state ->
            state.copy(
                permission = PermissionState.Idle,
                location = LocationState.Idle,
            )
        }
    }
}
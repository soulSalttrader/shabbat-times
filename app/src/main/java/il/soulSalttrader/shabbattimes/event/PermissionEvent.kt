package il.soulSalttrader.shabbattimes.event

import il.soulSalttrader.shabbattimes.location.LocationUiState
import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.reducer.LocationReducer
import il.soulSalttrader.shabbattimes.reducer.Reducible

sealed interface PermissionEvent : AppEvent, Reducible<LocationUiState> {
    data object ShowEducation : PermissionEvent {
        override val reducer = LocationReducer { state ->
            state.copy(permission = PermissionState.Education)
        }
    }

    data object Request : PermissionEvent {
        override val reducer = LocationReducer { state ->
            state.copy(permission = PermissionState.Requesting)
        }
    }

    data object AllGranted : PermissionEvent {
        override val reducer = LocationReducer { state ->
            state.copy(permission = PermissionState.Granted)
        }
    }

    data object DeniedWithRationale : PermissionEvent {
        override val reducer = LocationReducer { state ->
            state.copy(permission = PermissionState.Denied)
        }
    }

    data object DeniedPermanently : PermissionEvent {
        override val reducer = LocationReducer { state ->
            state.copy(permission = PermissionState.DeniedPermanently)
        }
    }

    data object AcceptedRationale : PermissionEvent {
        override val reducer = LocationReducer { state ->
            state.copy(permission = PermissionState.Requesting)
        }
    }

    data object DismissedRationale : PermissionEvent {
        override val reducer = LocationReducer { state ->
            state.copy(permission = PermissionState.Idle)
        }
    }

    data object RequestedAppSettings : PermissionEvent {
        override val reducer = LocationReducer { state ->
            state.copy(permission = PermissionState.Idle)
        }
    }

    data object ReturnedFromAppSettings : PermissionEvent {
        override val reducer = LocationReducer { state ->
            state.copy(permission = PermissionState.Requesting)
        }
    }
}
package il.soulSalttrader.shabbattimes.ui.event

import il.soulSalttrader.shabbattimes.model.LocationPermission
import il.soulSalttrader.shabbattimes.model.toPermissionState
import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.ui.permission.PermissionUiState
import il.soulSalttrader.shabbattimes.ui.reducer.PermissionReducer
import il.soulSalttrader.shabbattimes.ui.reducer.Reducible

sealed interface PermissionEvent : UiEvent, Reducible<PermissionUiState> {
    data object ShowEducation : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.Education)
        }
    }

    data object DismissEducation : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.Idle)
        }
    }

    data object Request : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.Requesting)
        }
    }

    data object SystemGranted : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.Granted)
        }
    }

    data object SystemDenied : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.DeniedRationale)
        }
    }

    data object SystemDeniedPermanently : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.DeniedPermanently)
        }
    }

    data object TappedCardDenied : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.DeniedRationale)
        }
    }

    data object TappedCardDeniedPermanently : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.DeniedPermanentlyRationale)
        }
    }

    data object DismissDeniedRationale : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.DeniedPermanently)
        }
    }

    data object DismissDeniedPermanently : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.DeniedPermanently)
        }
    }

    data object OpenAppSettings : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.DeniedPermanently)
        }
    }

    data object ReturnedFromAppSettings : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.Idle)
        }
    }

    data class PermissionChanged(val permission: LocationPermission) : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = permission.toPermissionState())
        }
    }
}
package il.soulSalttrader.shabbattimes.ui.event

import il.soulSalttrader.shabbattimes.model.LocationPermission
import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.ui.permission.PermissionUiState
import il.soulSalttrader.shabbattimes.ui.reducer.PermissionReducer
import il.soulSalttrader.shabbattimes.ui.reducer.Reducible

sealed interface PermissionEvent : AppEvent, Reducible<PermissionUiState> {
    data object ShowEducation : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.Education, isDialogVisible = true)
        }
    }

    data object Request : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.Requesting, isDialogVisible = true)
        }
    }

    data object AllGranted : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.Granted)
        }
    }

    data object DeniedWithRationale : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.Denied)
        }
    }

    data object DeniedPermanently : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.DeniedPermanently)
        }
    }

    data object AcceptedRationale : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.Requesting)
        }
    }

    data object DismissedRationale : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(isDialogVisible = false)
        }
    }

    data object RequestedAppSettings : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.DeniedPermanently)
        }
    }

    data object ShowDeniedPermanentlyDialog : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.DeniedPermanently, isDialogVisible = true)
        }
    }

    data object ReturnedFromAppSettings : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(permission = PermissionState.Requesting, isDialogVisible = true)
        }
    }

    data class PermissionChanged(val permission: LocationPermission) : PermissionEvent {
        override val reducer = PermissionReducer { state ->
            state.copy(
                permission = when (permission) {
                    is LocationPermission.Idle              -> PermissionState.Idle
                    is LocationPermission.Education         -> PermissionState.Education
                    is LocationPermission.Requesting        -> PermissionState.Requesting
                    is LocationPermission.Granted           -> PermissionState.Granted
                    is LocationPermission.Denied            -> PermissionState.Denied
                    is LocationPermission.DeniedPermanently -> PermissionState.DeniedPermanently
                },
            )
        }
    }
}
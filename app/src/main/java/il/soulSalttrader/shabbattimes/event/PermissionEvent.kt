package il.soulSalttrader.shabbattimes.event

import il.soulSalttrader.shabbattimes.model.ShabbatState
import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.reducer.Reducible
import il.soulSalttrader.shabbattimes.reducer.ShabbatReducer

sealed interface PermissionEvent : AppEvent, Reducible<ShabbatState> {
    data object Request : PermissionEvent {
        override val reducer = ShabbatReducer { state ->
            state.copy(permission = PermissionState.Requesting)
        }
    }

    data object AllGranted : PermissionEvent {
        override val reducer = ShabbatReducer { state ->
            state.copy(permission = PermissionState.Granted)
        }
    }

    data object DeniedWithRationale : PermissionEvent {
        override val reducer = ShabbatReducer { state ->
            state.copy(permission = PermissionState.Denied)
        }
    }

    data object DeniedPermanently : PermissionEvent {
        override val reducer = ShabbatReducer { state ->
            state.copy(permission = PermissionState.DeniedPermanently)
        }
    }

    data object AcceptedRationale : PermissionEvent {
        override val reducer = ShabbatReducer { state ->
            state.copy(permission = PermissionState.Requesting)
        }
    }

    data object DismissedRationale : PermissionEvent {
        override val reducer = ShabbatReducer { state ->
            state.copy(permission = PermissionState.Idle)
        }
    }

    data object RequestedAppSettings : PermissionEvent {
        override val reducer = ShabbatReducer { state ->
            state.copy(permission = PermissionState.Idle)
        }
    }

    data object ReturnedFromAppSettings : PermissionEvent {
        override val reducer = ShabbatReducer { state ->
            state.copy(permission = PermissionState.Requesting)
        }
    }
}
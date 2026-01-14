package il.soulSalttrader.retro.core.event

import il.soulSalttrader.retro.core.reducer.Reducible
import il.soulSalttrader.retro.core.reducer.ShabbatReducer
import il.soulSalttrader.retro.shabbatApp.model.ShabbatState
import il.soulSalttrader.retro.shabbatApp.permission.PermissionState

sealed interface PermissionEvent : AppEvent, Reducible<ShabbatState> {

    data class Request(val permissions: List<String>) : PermissionEvent {
        override val reducer = ShabbatReducer { state -> state.copy(permission = PermissionState.Requesting) }
    }

    sealed interface Result : PermissionEvent {
        data class Granted(val permissions: List<String>) : Result {
            override val reducer = ShabbatReducer { state -> state.copy(permission = PermissionState.Granted(permissions)) }
        }

        data class DeniedWithRationale(val permissions: List<String>) : Result {
            override val reducer = ShabbatReducer { state -> state.copy(permission = PermissionState.ShowRationale(permissions)) }
        }

        data class DeniedPermanently(val permissions: List<String>) : Result {
            override val reducer = ShabbatReducer { state -> state.copy(permission = PermissionState.ShowSettingsPrompt(permissions)) }
        }
    }

    data object RationaleDismissed : PermissionEvent {
        override val reducer = ShabbatReducer { state -> state.copy(permission = PermissionState.Idle) }
    }

    data object SettingsScreenClosed : PermissionEvent {
        override val reducer = ShabbatReducer { state -> state.copy(permission = PermissionState.Idle) }
    }
}
package il.soulSalttrader.retro.core.event

import il.soulSalttrader.retro.core.reducer.PermissionReducer
import il.soulSalttrader.retro.core.reducer.Reducible
import il.soulSalttrader.retro.shabbatApp.permission.PermissionState

sealed interface PermissionEvent : AppEvent, Reducible<PermissionState> {

    data class Request(val permissions: List<String>) : PermissionEvent {
        override val reducer = PermissionReducer { PermissionState.Requesting }
    }

    sealed interface Result : PermissionEvent {
        data class Granted(val permissions: List<String>) : Result {
            override val reducer = PermissionReducer { PermissionState.Granted(permissions) }
        }

        data class DeniedWithRationale(val permissions: List<String>) : Result {
            override val reducer = PermissionReducer { PermissionState.ShowRationale(permissions) }
        }

        data class DeniedPermanently(val permissions: List<String>) : Result {
            override val reducer = PermissionReducer { PermissionState.ShowSettingsPrompt(permissions) }
        }
    }

    data object RationaleDismissed : PermissionEvent {
        override val reducer = PermissionReducer { PermissionState.Idle }
    }

    data object SettingsScreenClosed : PermissionEvent {
        override val reducer = PermissionReducer { PermissionState.Idle }
    }
}
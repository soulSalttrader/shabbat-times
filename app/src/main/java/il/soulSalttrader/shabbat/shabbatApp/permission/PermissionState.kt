package il.soulSalttrader.retro.shabbatApp.permission

import il.soulSalttrader.retro.core.model.State

sealed interface PermissionState : State {
    data object Idle : PermissionState
    data object Requesting : PermissionState

    data object Granted : PermissionState
    data object Denied : PermissionState
    data object DeniedPermanently : PermissionState
}
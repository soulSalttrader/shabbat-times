package il.soulSalttrader.shabbattimes.permission

import il.soulSalttrader.shabbattimes.model.State

sealed interface PermissionState : State {
    data object Idle : PermissionState
    data object Requesting : PermissionState

    data object Granted : PermissionState
    data object Denied : PermissionState
    data object DeniedPermanently : PermissionState
}
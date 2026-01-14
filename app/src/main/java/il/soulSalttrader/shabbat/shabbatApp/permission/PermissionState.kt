package il.soulSalttrader.retro.shabbatApp.permission

import il.soulSalttrader.retro.core.model.State

sealed interface PermissionState : State {
    data object Idle : PermissionState
    data object Requesting : PermissionState

    data class Granted(val permissions: List<String>) : PermissionState
    data class ShowRationale(val permissions: List<String>) : PermissionState
    data class ShowSettingsPrompt(val permissions: List<String>) : PermissionState
}
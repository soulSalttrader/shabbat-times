package il.soulSalttrader.retro.shabbatApp.permission

sealed interface PermissionState {
    data object Requesting : PermissionState
    data class Explain(val permissions: List<String>) : PermissionState
    data class NeedsSettings(val permissions: List<String>) : PermissionState
}
package il.soulSalttrader.retro.shabbatApp.permission

sealed interface PermissionResult {
    data object Granted : PermissionResult
    data class Explain(val permissions: List<String>) : PermissionResult
    data class Blocked(val permissions: List<String>) : PermissionResult
}
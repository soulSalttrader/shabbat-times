package il.soulSalttrader.shabbattimes.permission

sealed interface PermissionResult {
    data object Granted : PermissionResult
    data class Explain(val permissions: List<String>) : PermissionResult
    data class Blocked(val permissions: List<String>) : PermissionResult
}
package il.soulSalttrader.retro.shabbatApp.permission

sealed interface PermissionResult {
    data object Granted : PermissionResult
    data class Explain(val permissions: List<String>) : PermissionResult
    data class Blocked(val permissions: List<String>) : PermissionResult
}

fun PermissionResult.toOutcome(): PermissionOutcome = when (this) {
    is PermissionResult.Granted -> PermissionOutcome.AllowFeature
    is PermissionResult.Explain -> PermissionOutcome.RequestAgain
    is PermissionResult.Blocked -> PermissionOutcome.OpenSettings
}
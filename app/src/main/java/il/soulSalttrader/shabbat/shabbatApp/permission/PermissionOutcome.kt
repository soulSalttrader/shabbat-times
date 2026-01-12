package il.soulSalttrader.retro.shabbatApp.permission

sealed interface PermissionOutcome {
    data object AllowFeature : PermissionOutcome
    data object RequestAgain : PermissionOutcome
    data object OpenSettings : PermissionOutcome
}

fun PermissionOutcome.toResult(permissions: List<String>) = when (this) {
    PermissionOutcome.AllowFeature -> PermissionResult.Granted
    PermissionOutcome.RequestAgain -> PermissionResult.Explain(permissions)
    PermissionOutcome.OpenSettings -> PermissionResult.Blocked(permissions)
}
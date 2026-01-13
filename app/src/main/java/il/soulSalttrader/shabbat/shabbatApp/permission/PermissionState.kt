package il.soulSalttrader.retro.shabbatApp.permission

data class PermissionState(
    val requested: Boolean = false,
    val explanationFor: List<String> = emptyList(),
    val openSettings: Boolean = false,
)
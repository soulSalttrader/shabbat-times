package il.soulSalttrader.retro.shabbatApp.permission

interface PermissionHandler {
    suspend fun request(permissions: List<String>): PermissionResult
}
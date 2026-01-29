package il.soulSalttrader.shabbattimes.permission

interface PermissionHandler {
    suspend fun request(permissions: List<String>): PermissionResult
}
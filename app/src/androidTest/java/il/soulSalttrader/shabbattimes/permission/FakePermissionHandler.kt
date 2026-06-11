package il.soulSalttrader.shabbattimes.permission

class FakePermissionHandler(
    private val granted: Boolean = false,
    private val requestResult: PermissionResult = PermissionResult.Granted,
) : PermissionHandler {
    override fun isGranted(permission: String) = granted
    override fun shouldShowRationale(permission: String) = !granted
    override suspend fun request(permissions: List<String>) = requestResult
}
package il.soulSalttrader.shabbattimes.permission

class FakePermissionHandler(
    private val granted: Set<String> = emptySet(),
    private val rationale: Set<String> = emptySet(),
) : PermissionHandler {
    override fun isGranted(permission: String) = permission in granted
    override fun shouldShowRationale(permission: String) = permission in rationale
    override suspend fun request(permissions: List<String>): PermissionResult {
        TODO("not needed for resolvePermissionEvent tests")
    }
}
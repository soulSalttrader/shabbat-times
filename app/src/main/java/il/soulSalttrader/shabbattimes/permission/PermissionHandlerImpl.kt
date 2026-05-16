package il.soulSalttrader.shabbattimes.permission

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PermissionHandlerImpl(
    private val checkPermission: (String) -> Boolean,
    private val checkShouldShowRationale: (String) -> Boolean,
    private val launch: (Array<String>) -> Unit,
) : PermissionHandler {
    override fun isGranted(permission: String): Boolean = checkPermission(permission)
    override fun shouldShowRationale(permission: String): Boolean = checkShouldShowRationale(permission)

    private var continuation: CancellableContinuation<PermissionResult>? = null

    override suspend fun request(permissions: List<String>): PermissionResult =
        suspendCancellableCoroutine { cont ->
            check(continuation == null) { "Permission request already in progress" }

            val missing = permissions.filterNot { isGranted(it) }

            if (missing.isEmpty()) {
                cont.resume(PermissionResult.Granted)
                return@suspendCancellableCoroutine
            }

            continuation = cont
            launch(missing.toTypedArray())

            cont.invokeOnCancellation {
                continuation = null
            }
        }

    fun onResult(result: Map<String, Boolean>) {
        val cont = continuation ?: return
        try {
            val denied = result.filterValues { !it }.keys.toList()
            val permanentlyDenied = denied.filterNot(checkShouldShowRationale)

            when {
                denied.isEmpty() -> {
                    cont.resume(PermissionResult.Granted)
                }

                permanentlyDenied.isNotEmpty() -> {
                    cont.resume(PermissionResult.Blocked(permissions = permanentlyDenied))
                }

                else -> {
                    cont.resume(PermissionResult.Explain(permissions = denied))
                }
            }
        } finally {
            continuation = null
        }
    }
}
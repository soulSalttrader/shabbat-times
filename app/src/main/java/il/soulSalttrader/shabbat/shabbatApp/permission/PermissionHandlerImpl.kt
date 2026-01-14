package il.soulSalttrader.retro.shabbatApp.permission

import android.util.Log
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PermissionHandlerImpl(
    private val isGranted: (String) -> Boolean,
    private val shouldShowRationale: (String) -> Boolean,
    private val launch: (Array<String>) -> Unit,
) : PermissionHandler {
    private var continuation: CancellableContinuation<PermissionState>? = null

    override suspend fun request(permissions: List<String>): PermissionState =
        suspendCancellableCoroutine { cont ->
            check(continuation == null) { "Permission request already in progress" }

            val missing = permissions.filterNot(isGranted)

            if (missing.isEmpty()) {
                cont.resume(PermissionState.Granted(permissions))
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

        Log.d("onResult", "granted: $result")

        try {
            val granted = result.filterValues { it }.keys.toList()
            val denied = result.filterValues { !it }.keys.toList()
            Log.d("onResult", "denied: $denied")
            val permanentlyDenied = denied.filterNot(shouldShowRationale)
            Log.d("onResult", "permanently: $permanentlyDenied")

            when {
                denied.isEmpty() -> {
                    cont.resume(PermissionState.Granted(permissions = granted))
                }

                permanentlyDenied.isNotEmpty() -> {
                    cont.resume(PermissionState.ShowSettingsPrompt(permissions = permanentlyDenied))
                }

                else -> {
                    cont.resume(PermissionState.ShowRationale(permissions = denied))
                }
            }
        } finally {
            continuation = null
        }
    }
}
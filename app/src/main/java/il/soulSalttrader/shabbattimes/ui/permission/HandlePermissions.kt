package il.soulSalttrader.shabbattimes.ui.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import il.soulSalttrader.shabbattimes.permission.PermissionResult
import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent

@Composable
fun HandlePermissions(
    permissions: List<String>,
    permissionState: PermissionUiState,
    dispatch: (PermissionEvent) -> Unit,
) {
    val permissionHandler = rememberPermissionHandler()

    LaunchedEffect(Unit) {
        val allGranted = permissions.all { permissionHandler.isGranted(it) }
        val anyPermanentlyDenied = permissions.any {
            !permissionHandler.isGranted(it) && !permissionHandler.shouldShowRationale(it)
        }
        val anyDenied = permissions.any {
            !permissionHandler.isGranted(it) && permissionHandler.shouldShowRationale(it)
        }

        when {
            allGranted -> dispatch(PermissionEvent.AllGranted)
            anyPermanentlyDenied -> dispatch(PermissionEvent.DeniedPermanently)
            anyDenied -> dispatch(PermissionEvent.DeniedWithRationale)
        }
    }

    LaunchedEffect(permissionState.permission) {
        if (permissionState.permission == PermissionState.Requesting) {
            val result = permissionHandler.request(permissions)

            when (result) {
                is PermissionResult.Granted -> dispatch(PermissionEvent.AllGranted)
                is PermissionResult.Explain -> dispatch(PermissionEvent.DeniedWithRationale)
                is PermissionResult.Blocked -> dispatch(PermissionEvent.DeniedPermanently)
            }
        }
    }
}
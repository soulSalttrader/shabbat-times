package il.soulSalttrader.shabbattimes.permission

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.ui.ExplanatoryDialog
import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent
import il.soulSalttrader.shabbattimes.ui.permission.PermissionUiState

@Composable
fun HandlePermissions(
    permissions: List<String>,
    permissionState: PermissionUiState,
    dispatch: (PermissionEvent) -> Unit,
) {
    val permissionHandler = rememberPermissionHandler()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val isGranted = permissions.all { permissionHandler.isGranted(it) }
        if (isGranted) dispatch(PermissionEvent.AllGranted)
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
package il.soulSalttrader.retro.shabbatApp.permission

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import il.soulSalttrader.retro.core.Debug

@Composable
fun HandlePermissions(
    permissions: List<String>,
    permissionState: PermissionState,
    onResult: (PermissionResult) -> Unit,
    onRationaleDismissed: () -> Unit,
    onRetry: () -> Unit,
) {
    val permissionHandler = rememberPermissionHandler()
    val context = LocalContext.current

    if (Debug.enabled) {
        Log.d(
            "PermCheck",
            "Granted = ${
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            }"
        )
    }

    when (permissionState) {
        is PermissionState.ShowRationale -> ExplanatoryDialog(
            onConfirm = onRetry,
            onConfirmText = "Allow",
            onDismiss = onRationaleDismissed,
        )

        is PermissionState.ShowSettingsPrompt -> ExplanatoryDialog(
            onConfirm = { context.openAppSettings() },
            onConfirmText = "Settings",
            onDismiss = onRationaleDismissed,
        )

        else                             -> Unit
    }

    LaunchedEffect(permissionState) {
        when (permissionState) {
            is PermissionState.Requesting    -> {
                val result = permissionHandler.request(permissions)
                onResult(result)
            }

            is PermissionState.ShowSettingsPrompt -> {
                context.openAppSettings()
            }

            else                             -> return@LaunchedEffect
        }
    }
}
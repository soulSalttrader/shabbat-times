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
    permissionState: PermissionUiState,
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

    LaunchedEffect(permissionState.requested) {
        if (!permissionState.requested) return@LaunchedEffect
        onResult(permissionHandler.request(permissions))
    }

    if (permissionState.explanationFor.isNotEmpty()) {
        ExplanatoryDialog(
            onConfirm = onRetry,
            onConfirmText = "Allow",
            onDismiss = onRationaleDismissed,
        )
    }

    if (permissionState.openSettings) {
        ExplanatoryDialog(
            onConfirm = { context.openAppSettings() },
            onConfirmText = "Settings",
            onDismiss = onRationaleDismissed,
        )
    }

    LaunchedEffect(permissionState.openSettings) {
        if (!permissionState.openSettings) return@LaunchedEffect
        context.openAppSettings()
    }
}
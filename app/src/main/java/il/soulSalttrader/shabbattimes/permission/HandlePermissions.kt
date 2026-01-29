package il.soulSalttrader.shabbattimes.permission

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.event.PermissionEvent

@Composable
fun HandlePermissions(
    permissions: List<String>,
    permissionState: PermissionState,
    dispatch: (PermissionEvent) -> Unit,
) {
    val permissionHandler = rememberPermissionHandler()
    val context = LocalContext.current

    LaunchedEffect(permissionState) {
        if (permissionState == PermissionState.Requesting) {
            val result = permissionHandler.request(permissions)

            when (result) {
                is PermissionResult.Granted ->
                    dispatch(PermissionEvent.AllGranted)

                is PermissionResult.Explain ->
                    dispatch(PermissionEvent.DeniedWithRationale)

                is PermissionResult.Blocked ->
                    dispatch(PermissionEvent.DeniedPermanently)
            }
        }
    }

    when (permissionState) {
        PermissionState.Denied            -> {
            ExplanatoryDialog(
                message = "We need location to show accurate zmanim times.",
                onConfirmText = "Allow",
                onConfirm = { dispatch(PermissionEvent.AcceptedRationale) },
                onDismiss = { dispatch(PermissionEvent.DismissedRationale) }
            )
        }
        PermissionState.DeniedPermanently -> {
            ExplanatoryDialog(
                message = "Location access was permanently denied. Please enable it in settings.",
                onConfirmText = "Open Settings",
                onConfirm = { dispatch(PermissionEvent.RequestedAppSettings) },
                onDismiss = { dispatch(PermissionEvent.DismissedRationale) }
            )
        }

        else -> Unit
    }

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
}
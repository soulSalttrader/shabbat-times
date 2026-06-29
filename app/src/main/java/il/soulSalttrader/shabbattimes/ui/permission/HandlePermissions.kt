package il.soulSalttrader.shabbattimes.ui.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import il.soulSalttrader.shabbattimes.permission.PermissionResult
import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent

@Composable
fun HandlePermissions(
    permissions: List<String>,
    permissionState: PermissionUiState,
    returnedFromSettings: Boolean,
    onSettingsHandled: () -> Unit,
    dispatch: (PermissionEvent) -> Unit,
) {
    val permissionHandler = rememberPermissionHandler()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        if (returnedFromSettings) {
            onSettingsHandled()

            val isGranted = permissions.all { permissionHandler.isGranted(it) }

            when (isGranted) {
                true -> dispatch(PermissionEvent.SystemGranted)
                else -> dispatch(PermissionEvent.ReturnedFromAppSettings)
            }
        }
    }

    LaunchedEffect(permissionState.permission) {
        if (permissionState.permission == PermissionState.Requesting) {
            val result = permissionHandler.request(permissions)

            when (result) {
                is PermissionResult.Granted -> dispatch(PermissionEvent.SystemGranted)
                is PermissionResult.Explain -> dispatch(PermissionEvent.SystemDenied)
                is PermissionResult.Blocked -> dispatch(PermissionEvent.SystemDeniedPermanently)
            }
        }
    }
}
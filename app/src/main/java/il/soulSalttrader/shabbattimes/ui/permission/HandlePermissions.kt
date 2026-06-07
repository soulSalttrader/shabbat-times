package il.soulSalttrader.shabbattimes.ui.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import il.soulSalttrader.shabbattimes.permission.PermissionResult
import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.permission.resolvePermissionEvent
import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent

@Composable
fun HandlePermissions(
    permissions: List<String>,
    permissionState: PermissionUiState,
    dispatch: (PermissionEvent) -> Unit,
) {
    val permissionHandler = rememberPermissionHandler()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        val resolved = permissionHandler.resolvePermissionEvent(permissions)

        when {
            resolved != null -> {
                dispatch(resolved)
            }
            permissionState.permission == PermissionState.DeniedPermanently -> {
                // User might have enabled permission in Settings
                dispatch(PermissionEvent.ReturnedFromAppSettings)
            }
        }
    }

    // Initial check only for already granted or needs rationale
    LaunchedEffect(Lifecycle.Event.ON_RESUME) {
        permissionHandler.resolvePermissionEvent(permissions)?.let(dispatch)
    }

    // Initial check when screen is first composed
    LaunchedEffect(Unit) {
        permissionHandler.resolvePermissionEvent(permissions)?.let(dispatch)
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
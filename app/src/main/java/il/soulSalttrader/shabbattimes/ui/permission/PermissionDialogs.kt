package il.soulSalttrader.shabbattimes.ui.permission

import androidx.compose.runtime.Composable
import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.ui.ExplanatoryDialog
import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent

@Composable
fun PermissionDialogs(
    permissionState: PermissionUiState,
    dispatch: (PermissionEvent) -> Unit,
) {
    if (!permissionState.isDialogVisible) return

    when (permissionState.permission) {
        PermissionState.Education -> ExplanatoryDialog(
            title = "Times, wherever you are",
            message = "To show candle lightning and havdalah time for your current position...",
            onConfirmText = "Continue",
            onConfirm = { dispatch(PermissionEvent.Request) },
            onDismissText = "Add manually instead",
            onDismiss = { dispatch(PermissionEvent.DismissedRationale) },
        )

        PermissionState.Denied    -> ExplanatoryDialog(
            message = "We need location to show accurate zmanim times.",
            onConfirmText = "Allow",
            onConfirm = { dispatch(PermissionEvent.AcceptedRationale) },
            onDismiss = { dispatch(PermissionEvent.DismissedRationale) },
        )

        PermissionState.DeniedPermanently -> ExplanatoryDialog(
            message = "Location access was permanently denied. Please enable it in settings.",
            onConfirmText = "Open Settings",
            onConfirm = { dispatch(PermissionEvent.RequestedAppSettings) },
            onDismiss = { dispatch(PermissionEvent.DismissedRationale) },
        )

        else -> Unit
    }
}
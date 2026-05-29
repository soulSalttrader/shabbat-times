package il.soulSalttrader.shabbattimes.ui.permission

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.TestTags.DENIED_PERMANENTLY_DIALOG
import il.soulSalttrader.shabbattimes.TestTags.EDUCATION_DIALOG
import il.soulSalttrader.shabbattimes.TestTags.RATIONALE_DIALOG
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
            title = stringResource(R.string.permission_education_title),
            message = stringResource(R.string.permission_education_message),
            onConfirmText = stringResource(R.string.permission_education_confirm),
            onConfirm = { dispatch(PermissionEvent.Request) },
            onDismissText = stringResource(R.string.permission_education_dismiss),
            onDismiss = { dispatch(PermissionEvent.DismissedRationale) },
            testTag = EDUCATION_DIALOG,
        )

        PermissionState.Denied    -> ExplanatoryDialog(
            message = stringResource(R.string.permission_denied_message),
            onConfirmText = stringResource(R.string.permission_denied_confirm),
            onConfirm = { dispatch(PermissionEvent.AcceptedRationale) },
            onDismiss = { dispatch(PermissionEvent.DismissedRationale) },
            testTag = RATIONALE_DIALOG,
        )

        PermissionState.DeniedPermanently -> ExplanatoryDialog(
            message = stringResource(R.string.permission_denied_permanently_message),
            onConfirmText = stringResource(R.string.permission_denied_permanently_confirm),
            onConfirm = { dispatch(PermissionEvent.RequestedAppSettings) },
            onDismiss = { dispatch(PermissionEvent.DismissedRationale) },
            testTag = DENIED_PERMANENTLY_DIALOG,
        )

        else -> Unit
    }
}
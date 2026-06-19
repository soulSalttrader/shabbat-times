package il.soulSalttrader.shabbattimes.ui.permission

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.TestTags.DENIED_PERMANENTLY_DIALOG
import il.soulSalttrader.shabbattimes.TestTags.EDUCATION_DIALOG
import il.soulSalttrader.shabbattimes.TestTags.RATIONALE_DIALOG
import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.ui.DialogButtonAction
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
            confirmAction = DialogButtonAction(
                text = stringResource(R.string.permission_education_confirm),
                onClick = { dispatch(PermissionEvent.Request) },
                color = { MaterialTheme.colorScheme.primary },
            ),
            dismissAction = DialogButtonAction(
                text = stringResource(R.string.permission_education_dismiss),
                onClick = { dispatch(PermissionEvent.DismissedRationale) },
                color = { MaterialTheme.colorScheme.error },
            ),
            testTag = EDUCATION_DIALOG,
        )

        PermissionState.Denied    -> ExplanatoryDialog(
            message = stringResource(R.string.permission_denied_message),
            confirmAction = DialogButtonAction(
                text = stringResource(R.string.permission_denied_confirm),
                onClick = { dispatch(PermissionEvent.AcceptedRationale) },
                color = { MaterialTheme.colorScheme.primary },
            ),
            dismissAction = DialogButtonAction(
                text = stringResource(R.string.dialog_default_dismiss),
                onClick = { dispatch(PermissionEvent.DismissedRationale) },
                color = { MaterialTheme.colorScheme.error },
            ),
            testTag = RATIONALE_DIALOG,
        )

        PermissionState.DeniedPermanently -> ExplanatoryDialog(
            message = stringResource(R.string.permission_denied_permanently_message),
            confirmAction = DialogButtonAction(
                text = stringResource(R.string.permission_denied_permanently_confirm),
                onClick = { dispatch(PermissionEvent.RequestedAppSettings) },
                color = { MaterialTheme.colorScheme.primary }
            ),
            dismissAction = DialogButtonAction(
                text = stringResource(R.string.dialog_default_dismiss),
                onClick = { dispatch(PermissionEvent.DismissedRationale) },
                color = { MaterialTheme.colorScheme.error },
            ),
            testTag = DENIED_PERMANENTLY_DIALOG,
        )

        else -> Unit
    }
}
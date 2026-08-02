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
    onOpenSettings: () -> Unit,
    dispatch: (PermissionEvent) -> Unit,
) {
    when (permissionState.permission) {
        PermissionState.Education -> ExplanatoryDialog(
            title = stringResource(R.string.permission_education_title),
            message = stringResource(R.string.permission_education_message),
            confirmAction = DialogButtonAction(
                text = stringResource(R.string.permission_education_confirm),
                onClick = { dispatch(PermissionEvent.RequestPermission) },
                color = { MaterialTheme.colorScheme.primary },
            ),
            dismissAction = DialogButtonAction(
                text = stringResource(R.string.permission_education_dismiss),
                onClick = { dispatch(PermissionEvent.DismissEducation) },
                color = { MaterialTheme.colorScheme.error },
            ),
            testTag = EDUCATION_DIALOG,
        )

        PermissionState.DeniedRationale -> ExplanatoryDialog(
            message = stringResource(R.string.permission_denied_message),
            confirmAction = DialogButtonAction(
                text = stringResource(R.string.permission_denied_confirm),
                onClick = { dispatch(PermissionEvent.RequestPermission) },
                color = { MaterialTheme.colorScheme.primary },
            ),
            dismissAction = DialogButtonAction(
                text = stringResource(R.string.dialog_default_dismiss),
                onClick = { dispatch(PermissionEvent.DismissDeniedRationale) },
                color = { MaterialTheme.colorScheme.error },
            ),
            testTag = RATIONALE_DIALOG,
        )

        PermissionState.DeniedPermanentlyRationale -> ExplanatoryDialog(
            message = stringResource(R.string.permission_denied_permanently_message),
            confirmAction = DialogButtonAction(
                text = stringResource(R.string.permission_denied_permanently_confirm),
                onClick = {
                    onOpenSettings()
                    dispatch(PermissionEvent.OpenAppSettings)
                },
                color = { MaterialTheme.colorScheme.primary },
            ),
            dismissAction = DialogButtonAction(
                text = stringResource(R.string.dialog_default_dismiss),
                onClick = { dispatch(PermissionEvent.DismissDeniedPermanently) },
                color = { MaterialTheme.colorScheme.error },
            ),
            testTag = DENIED_PERMANENTLY_DIALOG,
        )

        else -> Unit
    }
}

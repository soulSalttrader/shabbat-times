package il.soulSalttrader.shabbattimes.ui


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.TestTags.BUTTON_DIALOG_CONFIRM
import il.soulSalttrader.shabbattimes.TestTags.BUTTON_DIALOG_DISMISS

@Composable
fun ExplanatoryDialog(
    title: String = stringResource(R.string.dialog_default_title),
    message: String = stringResource(R.string.dialog_default_message),
    confirmAction: DialogButtonAction,
    dismissAction: DialogButtonAction? = null,
    testTag: String = "",
) {
    AlertDialog(
        modifier = Modifier
            .then(if (testTag.isNotEmpty()) Modifier.testTag(testTag) else Modifier),
        onDismissRequest = { dismissAction?.onClick?.invoke() ?: confirmAction.onClick() },
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = { DialogTextButton(confirmAction, BUTTON_DIALOG_CONFIRM) },
        dismissButton = { dismissAction?.let { DialogTextButton(it, BUTTON_DIALOG_DISMISS) } },
    )
}

@Composable
private fun DialogTextButton(action: DialogButtonAction, testTag: String) {
    TextButton(
        modifier = Modifier.testTag(testTag),
        onClick = action.onClick,
    ) {
        Text(
            text = action.text,
            color = action.color?.invoke() ?: MaterialTheme.colorScheme.primary,
        )
    }
}

@Preview
@Composable
fun PreviewExplanatoryDialog() {
    ExplanatoryDialog(
        message = "We need your location to show nearby data.",
        confirmAction = DialogButtonAction(
            text = "Ok",
            onClick = {},
        ),
    )
}
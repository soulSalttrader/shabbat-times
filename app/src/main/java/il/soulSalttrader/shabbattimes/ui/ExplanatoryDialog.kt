package il.soulSalttrader.shabbattimes.ui


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import il.soulSalttrader.shabbattimes.R

@Composable
fun ExplanatoryDialog(
    message: String = stringResource(R.string.dialog_default_message),
    title: String = stringResource(R.string.dialog_default_title),

    onConfirmText: String = stringResource(R.string.dialog_default_confirm),
    onConfirm: () -> Unit,
    onConfirmColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },

    onDismissText: String = stringResource(R.string.dialog_default_dismiss),
    onDismiss: () -> Unit,
    onDismissColor: @Composable () -> Color = { MaterialTheme.colorScheme.error },
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = onConfirmText,
                    color = onConfirmColor(),
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = onDismissText,
                    color = onDismissColor(),
                )
            }
        }
    )
}

@Preview
@Composable
fun PreviewExplanatoryDialog() {

    ExplanatoryDialog(
        message = "We need your location to show nearby data.",
        onConfirm = { /*no op*/ },
        onDismiss = { /*no op*/ },
    )
}
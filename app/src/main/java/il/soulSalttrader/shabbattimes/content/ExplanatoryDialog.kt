package il.soulSalttrader.shabbattimes.content


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ExplanatoryDialog(
    message: String = "We need your location to show nearby data.",
    title: String = "Permission required",

    onConfirmText: String = "Allow",
    onConfirm: () -> Unit,
    onConfirmColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },

    onDismissText: String = "Cancel",
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
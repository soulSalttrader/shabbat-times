package il.soulSalttrader.retro.shabbatApp.permission

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ExplanatoryDialog(
    message: String = "We need your location to show nearby data.",
    onConfirmText: String = "Allow",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Permission required") },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(onConfirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
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
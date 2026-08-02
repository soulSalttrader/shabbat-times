package il.soulSalttrader.shabbattimes.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.settings.CandleLightingOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : SettingsOption> SettingsOptionSheet(
    title: String,
    options: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))

            options.forEach { option ->
                SettingsOptionItem(
                    option = option,
                    selected = option == selected,
                    onClick = {
                        onSelect(option)
                        onDismiss()
                    },
                )
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Preview
@Composable
fun PreviewSettingsOptionSheet() {
    SettingsOptionSheet(
        title = "Tzeit Baal Hatanya",
        options = CandleLightingOffset.entries,
        selected = CandleLightingOffset.MIN_18,
        onSelect = {},
        onDismiss = {},
    )
}

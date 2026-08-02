package il.soulSalttrader.shabbattimes.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.settings.CandleLightingOffset
import il.soulSalttrader.shabbattimes.ui.uiIcon.UiIcon
import il.soulSalttrader.shabbattimes.ui.uiIcon.UiIconImage

@Composable
fun <T : SettingsOption> SettingsOptionRow(
    label: String,
    selected: T,
    onClick: () -> Unit,
) {
    SettingsRow(onClick = onClick) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.labelLarge)
            Text(
                text = "${stringResource(selected.titleRes)} (${selected.valueLabel})",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        UiIconImage(
            icon = UiIcon.Resource(R.drawable.chevron_right_24dp),
            contentDescription = "optionRowIcon",
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Preview
@Composable
fun PreviewSettingsOptionRow() {
    SettingsOptionRow(
        label = "Candle lightning time",
        selected = CandleLightingOffset.entries[0],
        onClick = {},
    )
}

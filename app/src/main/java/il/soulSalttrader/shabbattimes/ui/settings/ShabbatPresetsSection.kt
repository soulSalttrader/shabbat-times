package il.soulSalttrader.shabbattimes.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.settings.ShabbatPreset
import il.soulSalttrader.shabbattimes.ui.SectionHeader

@Composable
fun ShabbatPresetsSection(
    header: String = stringResource(R.string.settings_my_tradition),
    selected: ShabbatPreset,
    onPresetSelected: (ShabbatPreset) -> Unit,
) {
    SettingsSection {
        SectionHeader(header)
        TraditionRadioGroup(onPresetSelected, selected)
    }
}

@Composable
private fun TraditionRadioGroup(
    onPresetSelected: (ShabbatPreset) -> Unit,
    selected: ShabbatPreset,
) {
    ShabbatPreset.all.forEach { preset ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onPresetSelected(preset) }
                .padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = selected == preset,
                onClick = { onPresetSelected(preset) },
            )

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    text = stringResource(preset.labelRes),
                    style = MaterialTheme.typography.bodyLarge,
                )

                Text(
                    text = stringResource(
                        R.string.settings_preset_summary,
                        preset.candleLightingOffsetMinutes,
                        preset.havdalahOffsetMinutes
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
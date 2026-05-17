package il.soulSalttrader.shabbattimes.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
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
        SettingsRow(onClick = { onPresetSelected(preset) }) {
            TraditionLabel(preset)

            RadioButton(
                selected = selected == preset,
                onClick = { onPresetSelected(preset) },
            )
        }
    }
}

@Composable
private fun TraditionLabel(preset: ShabbatPreset) {
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
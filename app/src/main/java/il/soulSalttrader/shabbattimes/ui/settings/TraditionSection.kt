package il.soulSalttrader.shabbattimes.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.settings.ShabbatPreset
import il.soulSalttrader.shabbattimes.ui.SectionHeader

@Composable
fun TraditionSection(
    presets: List<ShabbatPreset>,
    selected: ShabbatPreset,
    onPresetSelected: (ShabbatPreset) -> Unit,
    header: String = stringResource(R.string.settings_my_tradition),
) {
    SettingsSection {
        SectionHeader(header)
        TraditionRadioGroup(presets, selected, onPresetSelected)
    }
}

@Composable
private fun TraditionRadioGroup(
    presets: List<ShabbatPreset>,
    selected: ShabbatPreset,
    onPresetSelected: (ShabbatPreset) -> Unit,
) {
    presets.forEach { preset ->
        SettingsRow(onClick = { onPresetSelected(preset) }) {
            TraditionLabel(preset)
            SquareCheckmark(selected = selected == preset)
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
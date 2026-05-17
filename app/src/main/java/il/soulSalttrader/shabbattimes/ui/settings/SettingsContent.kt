package il.soulSalttrader.shabbattimes.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.settings.AboutItemDisplay
import il.soulSalttrader.shabbattimes.settings.ShabbatPreset

@Composable
fun SettingsContent(
    items: List<AboutItemDisplay>,
    preset: ShabbatPreset,
    onPresetSelected: (ShabbatPreset) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        ShabbatPresetsSection(selected = preset, onPresetSelected = onPresetSelected)
        HorizontalDivider()
        AboutSection(items = items)
    }
}
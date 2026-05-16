package il.soulSalttrader.shabbattimes.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.settings.AboutItemDisplay
import il.soulSalttrader.shabbattimes.settings.ShabbatPreset

@Composable
fun SettingsContent(
    items: List<AboutItemDisplay>,
    state: SettingsUiState,
    onPresetSelected: (ShabbatPreset) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ShabbatPresetsSection(selected = state.preset, onPresetSelected = onPresetSelected)
        HorizontalDivider()
        AboutSection(items = items)
    }
}
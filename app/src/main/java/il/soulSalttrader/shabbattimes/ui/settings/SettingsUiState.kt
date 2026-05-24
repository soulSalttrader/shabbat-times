package il.soulSalttrader.shabbattimes.ui.settings

import il.soulSalttrader.shabbattimes.settings.ShabbatPreset
import il.soulSalttrader.shabbattimes.model.State

data class SettingsUiState(
    val preset: ShabbatPreset = ShabbatPreset.Ashkenazi,
) : State
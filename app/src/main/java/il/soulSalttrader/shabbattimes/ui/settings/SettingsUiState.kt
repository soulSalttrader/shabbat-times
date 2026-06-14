package il.soulSalttrader.shabbattimes.ui.settings

import il.soulSalttrader.shabbattimes.model.State
import il.soulSalttrader.shabbattimes.settings.ShabbatPreferences

data class SettingsUiState(
    val preferences: ShabbatPreferences = ShabbatDefaults.PREFERENCES,
) : State
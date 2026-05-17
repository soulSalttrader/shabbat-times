package il.soulSalttrader.shabbattimes.ui.event

import il.soulSalttrader.shabbattimes.settings.ShabbatPreset
import il.soulSalttrader.shabbattimes.ui.reducer.Reducible
import il.soulSalttrader.shabbattimes.ui.reducer.SettingsReducer
import il.soulSalttrader.shabbattimes.ui.settings.SettingsUiState

interface SettingsEvent : AppEvent, Reducible<SettingsUiState> {

    data class PresetSelected(val preset: ShabbatPreset) : SettingsEvent {
        override val reducer = SettingsReducer { state -> state.copy(preset = preset) }
    }
}
package il.soulSalttrader.shabbattimes.ui.event

import il.soulSalttrader.shabbattimes.settings.CandleLightingOffset
import il.soulSalttrader.shabbattimes.settings.HavdalahCriterion
import il.soulSalttrader.shabbattimes.ui.reducer.Reducible
import il.soulSalttrader.shabbattimes.ui.reducer.SettingsReducer
import il.soulSalttrader.shabbattimes.ui.settings.SettingsUiState

interface SettingsEvent : UiEvent, Reducible<SettingsUiState> {
    data class SetCandleLightingOffset(val offset: CandleLightingOffset) : SettingsEvent {
        override val reducer = SettingsReducer { state ->
            state.copy(preferences = state.preferences.copy(candleLightingOffset = offset))
        }
    }

    data class SetHavdalahCriterion(val criterion: HavdalahCriterion) : SettingsEvent {
        override val reducer = SettingsReducer { state ->
            state.copy(preferences = state.preferences.copy(havdalahCriterion = criterion))
        }
    }
}

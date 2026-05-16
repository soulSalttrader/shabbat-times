package il.soulSalttrader.shabbattimes.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import il.soulSalttrader.shabbattimes.ui.event.AppEvent
import il.soulSalttrader.shabbattimes.ui.event.SettingsEvent
import il.soulSalttrader.shabbattimes.ui.settings.SettingsUiState
import il.soulSalttrader.shabbattimes.useCase.GetShabbatPresetUseCase
import il.soulSalttrader.shabbattimes.useCase.SaveShabbatPresetUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

class SettingsViewModel @Inject constructor(
    private val getShabbatPreset: GetShabbatPresetUseCase,
    private val saveShabbatPreset: SaveShabbatPresetUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<SettingsUiState> = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init { dispatch(SettingsEvent.LoadPreset) }

    fun dispatch(event: AppEvent) {
        val newState = _state.updateAndGet { current ->
            when (event) {
                is SettingsEvent -> event.reducer reduce current
                else             -> current
            }
        }

        when (event) {
            is SettingsEvent.PresetSelected -> handlePresetSelected(newState)
            is SettingsEvent.LoadPreset     -> handleLoadPreset()
            else                            -> Unit
        }
    }

    private fun handlePresetSelected(newState: SettingsUiState) {
        viewModelScope.launch {
            saveShabbatPreset(newState.preset)
        }
    }

    private fun handleLoadPreset() {
        viewModelScope.launch {
            val preset = getShabbatPreset()
            dispatch(SettingsEvent.PresetLoaded(preset))
        }
    }
}
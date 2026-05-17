package il.soulSalttrader.shabbattimes.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.repository.UserPreferencesRepository
import il.soulSalttrader.shabbattimes.ui.event.AppEvent
import il.soulSalttrader.shabbattimes.ui.event.SettingsEvent
import il.soulSalttrader.shabbattimes.ui.settings.SettingsUiState
import il.soulSalttrader.shabbattimes.useCase.SaveShabbatPresetUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val saveShabbatPreset: SaveShabbatPresetUseCase,
    repository: UserPreferencesRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<SettingsUiState> = MutableStateFlow(value = SettingsUiState())

    val state: StateFlow<SettingsUiState> = repository.shabbatPreset
        .map { SettingsUiState(preset = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState()
        )

    fun dispatch(event: AppEvent) {
        val newState = _state.updateAndGet { current ->
            when (event) {
                is SettingsEvent -> event.reducer reduce current
                else             -> current
            }
        }

        when (event) {
            is SettingsEvent.PresetSelected -> handlePresetSelected(newState)
            else                            -> Unit
        }
    }

    private fun handlePresetSelected(newState: SettingsUiState) {
        viewModelScope.launch {
            saveShabbatPreset(newState.preset)
        }
    }
}
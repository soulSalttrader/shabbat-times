package il.soulSalttrader.shabbattimes.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.repository.UserPreferencesRepository
import il.soulSalttrader.shabbattimes.ui.event.UiEvent
import il.soulSalttrader.shabbattimes.ui.event.SettingsEvent
import il.soulSalttrader.shabbattimes.ui.settings.SettingsUiState
import il.soulSalttrader.shabbattimes.useCase.SaveShabbatPreferenceUseCase
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
    private val saveShabbatPreferences: SaveShabbatPreferenceUseCase,
    repository: UserPreferencesRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<SettingsUiState> = MutableStateFlow(value = SettingsUiState())

    val state: StateFlow<SettingsUiState> = repository.shabbatPreferences
        .map { SettingsUiState(preferences = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState()
        )

    fun dispatch(event: UiEvent) {
        _state.updateAndGet { current ->
            when (event) {
                is SettingsEvent -> event.reducer reduce current
                else             -> current
            }
        }

        when (event) {
            is SettingsEvent.SetCandleLightingOffset -> handleCandleLightningOffsetSelected(event)
            is SettingsEvent.SetHavdalahCriterion    -> handleHavdalahCriterionSelected(event)
            else                                     -> Unit
        }
    }

    private fun handleHavdalahCriterionSelected(event: SettingsEvent.SetHavdalahCriterion) {
        viewModelScope.launch {
            saveShabbatPreferences(event.criterion)
        }
    }

    private fun handleCandleLightningOffsetSelected(event: SettingsEvent.SetCandleLightingOffset) {
        viewModelScope.launch {
            saveShabbatPreferences(event.offset)
        }
    }
}
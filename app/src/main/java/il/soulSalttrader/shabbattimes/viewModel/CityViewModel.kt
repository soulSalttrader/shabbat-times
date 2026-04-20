package il.soulSalttrader.shabbattimes.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.content.city.CityUiState
import il.soulSalttrader.shabbattimes.content.normalizedOrNull
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.event.CityEvent
import il.soulSalttrader.shabbattimes.repository.CityRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

@HiltViewModel
class CityViewModel @Inject constructor(
    private val cityRepository: CityRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<CityUiState> = MutableStateFlow(value = CityUiState())
    val state: StateFlow<CityUiState> = _state

    fun dispatch(event: AppEvent) {
        val newState = _state.updateAndGet { current ->
            when (event) {
                is CityEvent -> event.reducer reduce current
                else         -> current
            }
        }

        when (event) {
            is CityEvent.CityDeleted -> handleDeleteTime(newState)
            else                     -> Unit
        }
    }

    private fun handleDeleteTime(state: CityUiState) {
        val city = state.selectedCity.normalizedOrNull() ?: return

        viewModelScope.launch {
            cityRepository.removeCity(city)
        }
    }
}
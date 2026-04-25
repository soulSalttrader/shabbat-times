package il.soulSalttrader.shabbattimes.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.content.city.CityUiState
import il.soulSalttrader.shabbattimes.content.normalizedOrNull
import il.soulSalttrader.shabbattimes.effect.AppEffect
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.event.CityEvent
import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.network.onFailure
import il.soulSalttrader.shabbattimes.network.onSuccess
import il.soulSalttrader.shabbattimes.repository.LocationRepository
import il.soulSalttrader.shabbattimes.useCase.RemoveCityUseCase
import il.soulSalttrader.shabbattimes.useCase.ResolveCurrentCityUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

@HiltViewModel
class CityViewModel @Inject constructor(
    locationRepository: LocationRepository,
    private val resolveCurrentCity: ResolveCurrentCityUseCase,
    private val removeCity: RemoveCityUseCase,
) : ViewModel() {

    private val _effects: MutableSharedFlow<AppEffect> = MutableSharedFlow(extraBufferCapacity = 20)
    val effects: SharedFlow<AppEffect> = _effects.asSharedFlow()

    private val _state: MutableStateFlow<CityUiState> = MutableStateFlow(value = CityUiState())

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val cityFlow: Flow<City?> = locationRepository.location
        .flatMapLatest { location ->
            location?.let {
                flow<City?> {
                    resolveCurrentCity(location)
                        .onSuccess("CityVM") { city -> emit(city) }
                        .onFailure("CityVM") { _effects.tryEmit(AppEffect.ShowToast("Network failed")) }
                }
            } ?: flowOf(null)
        }
        .catch { throwable ->
            _effects.tryEmit(AppEffect.ShowToast("Unexpected error: ${throwable.message}"))
            emit(null)
        }

    val state: StateFlow<CityUiState> = combine(
        _state,
        cityFlow,
    ) { state, city ->
        city?.let { CityEvent.CurrentCityLoaded(it).reducer reduce state } ?: state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CityUiState()
    )

    fun dispatch(event: AppEvent) {
        val newState = _state.updateAndGet { current ->
            when (event) {
                is CityEvent -> event.reducer reduce current
                else         -> current
            }
        }

        when (event) {
            is CityEvent.CityDeleted -> handleDeleteCity(newState)
            else                     -> Unit
        }
    }

    private fun handleDeleteCity(state: CityUiState) {
        val city = state.selectedCity.normalizedOrNull() ?: return

        viewModelScope.launch {
            removeCity(city)
        }
    }
}
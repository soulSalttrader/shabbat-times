package il.soulSalttrader.shabbattimes.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.content.shabbat.ShabbatUiState
import il.soulSalttrader.shabbattimes.effect.AppEffect
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.event.LocationEvent
import il.soulSalttrader.shabbattimes.event.PermissionEvent
import il.soulSalttrader.shabbattimes.event.ShabbatDataEvent
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay
import il.soulSalttrader.shabbattimes.network.onFailure
import il.soulSalttrader.shabbattimes.network.onSuccess
import il.soulSalttrader.shabbattimes.repository.CityRepository
import il.soulSalttrader.shabbattimes.repository.ShabbatRepository
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import kotlinx.coroutines.flow.update

@HiltViewModel
class ShabbatViewModel @Inject constructor(
    private val shabbatRepository: ShabbatRepository,
    private val cityRepository: CityRepository,
) : ViewModel() {
    private val _effects: MutableSharedFlow<AppEffect> = MutableSharedFlow(extraBufferCapacity = 20)
    val effects: SharedFlow<AppEffect> = _effects.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val halachicTimesFlow: StateFlow<List<HalachicTimesDisplay>> =
        cityRepository.cities
            .flatMapLatest { cities ->
                cities.takeUnless { it.isEmpty() }
                    ?.let { nonEmptyCities ->
                        flow {
                            val results = shabbatRepository.getHalachicTimes(nonEmptyCities)
                            val successes = mutableListOf<HalachicTimesDisplay>()

                            results.forEach { result ->
                                result
                                    .onSuccess(tag = "ShabbatVM") { halachicTimesDisplay ->
                                        successes.add(halachicTimesDisplay)
                                    }
                                    .onFailure(tag = "ShabbatVM") { e ->
                                        _effects.tryEmit(value = AppEffect.ShowToast(message = "Network failed"))
                                    }
                            }
                            emit(value = successes)
                        }
                    } ?: flowOf(emptyList())
            }
            .catch {throwable ->
                _effects.tryEmit(value = AppEffect.ShowToast(message = "Unexpected error: ${throwable.message}"))
                emit(value = emptyList())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList(),
            )

    private val _state: MutableStateFlow<ShabbatUiState> =
        MutableStateFlow(value = ShabbatUiState())

    val state: StateFlow<ShabbatUiState> = combine(
        flow = _state,
        flow2 = halachicTimesFlow,
    ) { state, halachicTimes ->
        ShabbatDataEvent.TimesLoaded(halachicTimes).reducer reduce state
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ShabbatUiState(),
        )

    fun dispatch(event: AppEvent) {
        _state.update { current ->
            when (event) {
                is ShabbatDataEvent -> event.reducer reduce current
                is PermissionEvent  -> event.reducer reduce current
                is LocationEvent    -> event.reducer reduce current
                else                -> current
            }
        }

        when (event) {
            is PermissionEvent.RequestedAppSettings -> _effects.tryEmit(AppEffect.OpenAppSettings)
            else                                    -> Unit
        }
    }
}
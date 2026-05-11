package il.soulSalttrader.shabbattimes.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.di.InMemory
import il.soulSalttrader.shabbattimes.model.HalachicTimes
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.repository.CurrentLocationRepository
import il.soulSalttrader.shabbattimes.repository.PermissionRepository
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepository
import il.soulSalttrader.shabbattimes.ui.effect.AppEffect
import il.soulSalttrader.shabbattimes.ui.event.AppEvent
import il.soulSalttrader.shabbattimes.ui.event.ShabbatEvent
import il.soulSalttrader.shabbattimes.ui.shabbat.ShabbatResultState
import il.soulSalttrader.shabbattimes.ui.shabbat.ShabbatUiState
import il.soulSalttrader.shabbattimes.useCase.GetHalachicTimesUseCase
import il.soulSalttrader.shabbattimes.useCase.RemoveSavedLocationUseCase
import il.soulSalttrader.shabbattimes.useCase.ReorderLocationsUseCase
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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

@HiltViewModel
class ShabbatViewModel @Inject constructor(
    @param:InMemory private val savedLocationsRepository: SavedLocationsRepository,
    @param:InMemory private val currentLocationRepository: CurrentLocationRepository,
    private val reorderLocationsUseCase: ReorderLocationsUseCase,
    private val getHalachicTimesUseCase: GetHalachicTimesUseCase,
    private val removeLocationUseCase: RemoveSavedLocationUseCase,
    permissionRepository: PermissionRepository,
) : ViewModel() {
    private val _effects: MutableSharedFlow<AppEffect> = MutableSharedFlow(extraBufferCapacity = 20)
    val effects: SharedFlow<AppEffect> = _effects.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val halachicTimesFlow: StateFlow<List<HalachicTimes>> = combine(
        currentLocationRepository.location,
        savedLocationsRepository.locations,
    ) { gpsLocation, savedLocations ->
        buildList {
            gpsLocation?.let { add(it) }
            addAll(savedLocations)
        }
    }.flatMapLatest { savedLocations ->
        flow {
            val results = getHalachicTimesUseCase(savedLocations)
            val successes = results.filterIsInstance<NetworkResult.Success<HalachicTimes>>()
                .map { it.data }

            if (results.any { it is NetworkResult.Failure }) {
                _effects.tryEmit(AppEffect.ShowToast("Some times failed to load"))
            }

            emit(successes)
        }
    }
        .catch { throwable ->
            dispatch(ShabbatEvent.ShabbatEntryLoadFailed(throwable.message ?: "Unknown error", throwable))
            _effects.tryEmit(AppEffect.ShowToast("Unexpected error: ${throwable.message}"))
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    private val _state: MutableStateFlow<ShabbatUiState> = MutableStateFlow(value = ShabbatUiState())

    val state: StateFlow<ShabbatUiState> = combine(
        _state,
        halachicTimesFlow,
        currentLocationRepository.location,
        savedLocationsRepository.locations,
        permissionRepository.permissionState,
    ) { state, halachicTimes, currentLocation, savedLocations, permission ->
        ShabbatEvent.ShabbatEntryLoaded(savedLocations, currentLocation, halachicTimes, permission).reducer reduce state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ShabbatUiState(),
    )

    fun dispatch(event: AppEvent) {
        _state.updateAndGet { current ->
            when (event) {
                is ShabbatEvent -> event.reducer reduce current
                else            -> current
            }
        }

        when (event) {
            is ShabbatEvent.LocationDeleted  -> handleDeleteLocation(event)
            is ShabbatEvent.ReorderLocations -> handleReorderLocations(event)
            else                             -> Unit
        }
    }

    private fun handleReorderLocations(event: ShabbatEvent.ReorderLocations) {
        viewModelScope.launch {
            val entries = (state.value.shabbat as? ShabbatResultState.Ready)?.entries ?: return@launch
            reorderLocationsUseCase(entries, event.from, event.to)
        }
    }

    private fun handleDeleteLocation(event: ShabbatEvent.LocationDeleted) {
        viewModelScope.launch {
            removeLocationUseCase(event.savedLocation, event.isCurrent)
        }
    }
}
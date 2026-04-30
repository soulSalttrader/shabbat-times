package il.soulSalttrader.shabbattimes.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.content.shabbat.ShabbatUiState
import il.soulSalttrader.shabbattimes.effect.AppEffect
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.event.ShabbatDataEvent
import il.soulSalttrader.shabbattimes.model.HalachicTimes
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.repository.PermissionRepository
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepository
import il.soulSalttrader.shabbattimes.useCase.GetHalachicTimesUseCase
import il.soulSalttrader.shabbattimes.useCase.RemoveCityUseCase
import il.soulSalttrader.shabbattimes.useCase.ResolveGpsLocationUseCase
import il.soulSalttrader.shabbattimes.useCase.SaveLocationUseCase
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

@HiltViewModel
class ShabbatViewModel @Inject constructor(
    private val savedLocationsRepository: SavedLocationsRepository,
    private val permissionRepository: PermissionRepository,
    private val saveGpsLocationUseCase: SaveLocationUseCase,
    private val resolveGpsLocationUseCase: ResolveGpsLocationUseCase,
    private val getHalachicTimesUseCase: GetHalachicTimesUseCase,
    private val removeLocationUseCase: RemoveCityUseCase,
) : ViewModel() {
    private val _effects: MutableSharedFlow<AppEffect> = MutableSharedFlow(extraBufferCapacity = 20)
    val effects: SharedFlow<AppEffect> = _effects.asSharedFlow()

    private val gpsLocationFlow: StateFlow<SavedLocation?> = resolveGpsLocationUseCase()
        .onStart { dispatch(ShabbatDataEvent.GpsLocationRequested) }
        .catch { e ->
            dispatch(ShabbatDataEvent.GpsLocationError(e.message ?: "Unknown error"))
            _effects.tryEmit(AppEffect.ShowToast(e.message ?: "Unknown error"))
        }
        .map { resolved ->
            resolved?.let {
                SavedLocation(
                    id = SavedLocation.GPS_ID,
                    name = resolved.name,
                    coordinates = resolved.coordinates,
                    timeZoneId = resolved.timeZoneId,
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null,
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val halachicTimesFlow: StateFlow<List<HalachicTimes>> = combine(
        gpsLocationFlow,
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
        gpsLocationFlow,
        halachicTimesFlow,
        savedLocationsRepository.locations,
        permissionRepository.permissionState,
    ) { state, gpsLocation, halachicTimes, savedLocations, permission ->
        val withPermission = ShabbatDataEvent.GpsPermissionChanged(permission).reducer reduce state
        ShabbatDataEvent.LocationWithTimesLoaded(savedLocations, halachicTimes, gpsLocation).reducer reduce withPermission
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ShabbatUiState(),
    )

    fun dispatch(event: AppEvent) {
        _state.updateAndGet { current ->
            when (event) {
                is ShabbatDataEvent -> event.reducer reduce current
                else                -> current
            }
        }

        when (event) {
            is ShabbatDataEvent.GpsLocationRequested -> handleGpsLocationRequested()
            is ShabbatDataEvent.LocationDeleted      -> handleDeleteLocation(event)
            else                                     -> Unit
        }
    }

    private fun handleGpsLocationRequested() {
        viewModelScope.launch {
            val gpsLocation = gpsLocationFlow.value ?: return@launch
            saveGpsLocationUseCase(gpsLocation)
        }
    }

    private fun handleDeleteLocation(event: ShabbatDataEvent.LocationDeleted) {
        viewModelScope.launch {
            removeLocationUseCase(event.savedLocation, event.isCurrent)
        }
    }
}
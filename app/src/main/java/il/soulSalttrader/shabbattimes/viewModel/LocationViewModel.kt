package il.soulSalttrader.shabbattimes.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.effect.AppEffect
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.event.LocationEvent
import il.soulSalttrader.shabbattimes.location.LocationStatus
import il.soulSalttrader.shabbattimes.location.LocationUiModel
import il.soulSalttrader.shabbattimes.location.LocationUiState
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.repository.PermissionRepository
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepository
import il.soulSalttrader.shabbattimes.useCase.RemoveCityUseCase
import il.soulSalttrader.shabbattimes.useCase.ResolveGpsLocationUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

@HiltViewModel
class LocationViewModel @Inject constructor(
    savedLocationsRepository: SavedLocationsRepository,
    resolveGpsLocationUseCase: ResolveGpsLocationUseCase,
    permissionRepository: PermissionRepository,
    private val removeLocation: RemoveCityUseCase,
) : ViewModel() {

    private val _effects: MutableSharedFlow<AppEffect> = MutableSharedFlow(extraBufferCapacity = 20)
    val effects: SharedFlow<AppEffect> = _effects.asSharedFlow()

    private val _state: MutableStateFlow<LocationUiState> =
        MutableStateFlow(value = LocationUiState())

    private val gpsLocationFlow: Flow<LocationUiModel?> = resolveGpsLocationUseCase()
        .onStart { dispatch(LocationEvent.GpsLocationRequested) }
        .catch { e ->
            dispatch(LocationEvent.GpsLocationError(e.message ?: "Unknown error"))
            _effects.tryEmit(AppEffect.ShowToast(e.message ?: "Unknown error"))
        }
        .map { resolved ->
            resolved?.let {
                LocationUiModel(
                    location = SavedLocation(
                        id = "gps",
                        name = resolved.name,
                        coordinates = resolved.coordinates,
                        timeZoneId = resolved.timeZoneId,
                    ),
                    status = LocationStatus.Current,
                    times = HalachicTimesDisplay(),
                )
            }
        }

    val state: StateFlow<LocationUiState> = combine(
        _state,
        savedLocationsRepository.locations,
        gpsLocationFlow,
        permissionRepository.permissionState,
    ) { state, savedLocations, gpsLocation, permission ->
        val withPermission = LocationEvent.GpsPermissionChanged(permission).reducer reduce state
        LocationEvent.LocationLoaded(gpsLocation, savedLocations).reducer reduce withPermission
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LocationUiState()
    )

    fun dispatch(event: AppEvent) {
        val newState = _state.updateAndGet { current ->
            when (event) {
                is LocationEvent -> event.reducer reduce current
                else             -> current
            }
        }

        when (event) {
            is LocationEvent.LocationDeleted -> handleDeleteLocation(event)
            else                             -> newState
        }
    }

    fun handleDeleteLocation(event: LocationEvent.LocationDeleted) {
        viewModelScope.launch {
            removeLocation(event.savedLocation, event.isCurrent)
        }
    }
}
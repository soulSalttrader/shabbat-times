package il.soulSalttrader.shabbattimes.viewModel

import android.location.Location
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.effect.AppEffect
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.event.LocationEvent
import il.soulSalttrader.shabbattimes.event.PermissionEvent
import il.soulSalttrader.shabbattimes.location.LocationPermission
import il.soulSalttrader.shabbattimes.location.LocationStatus
import il.soulSalttrader.shabbattimes.location.LocationUiState
import il.soulSalttrader.shabbattimes.repository.CityRepository
import il.soulSalttrader.shabbattimes.repository.LocationRepository
import il.soulSalttrader.shabbattimes.repository.PermissionRepository
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.updateAndGet

@HiltViewModel
class LocationViewModel @Inject constructor(
    cityRepository: CityRepository,
    private val locationRepository: LocationRepository,
    private val permissionRepository: PermissionRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<LocationUiState> = MutableStateFlow(LocationUiState())

    private val _effects: MutableSharedFlow<AppEffect> = MutableSharedFlow(extraBufferCapacity = 20)
    val effects: SharedFlow<AppEffect> = _effects.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val locationFlow: Flow<Location?> = permissionRepository.permissionState
        .flatMapLatest { permission ->
            when (permission) {
                is LocationPermission.Granted -> locationRepository.location
                else                          -> flowOf(null)
            }
        }

    val state: StateFlow<LocationUiState> = combine(
        _state,
        locationFlow,
    ) { state, location ->
        location?.let { LocationEvent.LocationLoaded(it).reducer reduce state } ?: state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LocationUiState()
    )

    fun dispatch(event: AppEvent) {
        _state.updateAndGet { current ->
            when (event) {
                is LocationEvent -> event.reducer reduce current
                is PermissionEvent  -> event.reducer reduce current
                else             -> current
            }
        }

        when (event) {
            is PermissionEvent.AllGranted           -> permissionRepository.updatePermissionState(LocationPermission.Granted)
            is PermissionEvent.DeniedPermanently    -> permissionRepository.updatePermissionState(LocationPermission.Denied)
            is PermissionEvent.DeniedWithRationale  -> permissionRepository.updatePermissionState(LocationPermission.Denied)
            is PermissionEvent.RequestedAppSettings -> _effects.tryEmit(AppEffect.OpenAppSettings)
            else -> Unit
        }
    }

    private val currentCityObserver = cityRepository.cities
        .map { cities -> cities.none { it.locationStatus == LocationStatus.Current } }
        .distinctUntilChanged()
        .filter { it }
        .onEach {
            dispatch(LocationEvent.CurrentLocationRemoved)
            permissionRepository.updatePermissionState(LocationPermission.Denied)
        }
        .launchIn(scope = viewModelScope)
}
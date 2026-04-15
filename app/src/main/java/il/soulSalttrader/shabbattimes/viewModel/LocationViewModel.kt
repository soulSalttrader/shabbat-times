package il.soulSalttrader.shabbattimes.viewModel

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.effect.AppEffect
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.event.LocationEvent
import il.soulSalttrader.shabbattimes.event.PermissionEvent
import il.soulSalttrader.shabbattimes.location.LocationStatus
import il.soulSalttrader.shabbattimes.location.LocationUiState
import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.network.onFailure
import il.soulSalttrader.shabbattimes.network.onSuccess
import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.repository.CityRepository
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.updateAndGet

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repository: CityRepository,
    private val fusedClient: FusedLocationProviderClient,
) : ViewModel() {

    private val _state: MutableStateFlow<LocationUiState> = MutableStateFlow(LocationUiState())

    private val _effects: MutableSharedFlow<AppEffect> = MutableSharedFlow(extraBufferCapacity = 20)
    val effects: SharedFlow<AppEffect> = _effects.asSharedFlow()

    private val permissionFlow: Flow<PermissionState> = _state
        .map { state -> state.permission }
        .distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val locationFlow: Flow<Location?> = permissionFlow
        .flatMapLatest { permission ->
            when (permission) {
                is PermissionState.Granted -> currentLocationFlow()
                else -> flowOf(null)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val cityFlow: Flow<City?> = locationFlow
        .flatMapLatest { location ->
            location?.let {
                flow<City?> {
                    repository.geocodeReverse(it.latitude, it.longitude)
                        .onSuccess("LocationVM") { city ->
                            repository.setCurrentCity(city.copy(locationStatus = LocationStatus.Current))
                            emit(city)
                        }
                        .onFailure("LocationVM") { e ->
                            _effects.tryEmit(AppEffect.ShowToast(message = "Network failed"))
                        }
                }
            } ?: flowOf(null)
        }
        .catch { throwable ->
            _effects.tryEmit(AppEffect.ShowToast("Unexpected error: ${throwable.message}"))
            emit(null)
        }

    val state: StateFlow<LocationUiState> = combine(
        _state,
        locationFlow,
        cityFlow,
    ) { state, location, _ ->
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
            is PermissionEvent.RequestedAppSettings -> _effects.tryEmit(AppEffect.OpenAppSettings)
            else -> Unit
        }
    }

    @SuppressLint("MissingPermission")
    private fun currentLocationFlow(
        priority: Int = Priority.PRIORITY_HIGH_ACCURACY,
        interval: Long = 1L,
    ): Flow<Location> = callbackFlow {
        val locationRequest = LocationRequest.Builder(priority, interval)
            .setMinUpdateDistanceMeters(1000f)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.locations.lastOrNull() ?: return
                trySend(location)
            }
        }

        fusedClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper(),
        )

        awaitClose { fusedClient.removeLocationUpdates(callback) }
    }
}
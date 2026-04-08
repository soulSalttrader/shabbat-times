package il.soulSalttrader.shabbattimes.viewModel

import android.annotation.SuppressLint
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.event.LocationEvent
import il.soulSalttrader.shabbattimes.location.LocationStatus
import il.soulSalttrader.shabbattimes.location.LocationUiState
import il.soulSalttrader.shabbattimes.network.onFailure
import il.soulSalttrader.shabbattimes.network.onSuccess
import il.soulSalttrader.shabbattimes.repository.CityRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repository: CityRepository,
    private val fusedClient: FusedLocationProviderClient,
) : ViewModel() {

    private val _state: MutableStateFlow<LocationUiState> = MutableStateFlow(LocationUiState())
    val state: StateFlow<LocationUiState> = _state.asStateFlow()

    fun dispatch(event: AppEvent) {
        val newState = _state.updateAndGet { current ->
            when (event) {
                is LocationEvent -> event.reducer reduce current
                else             -> current
            }
        }

        when (event) {
            is LocationEvent.LocationLoaded -> handleLocationLoaded()
            else -> newState
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleLocationLoaded(
        priority: Int = Priority.PRIORITY_HIGH_ACCURACY,
        interval: Long = 10_000L,
    ) {
        val locationRequest = LocationRequest.Builder(priority,interval)
            .setMaxUpdates(1)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.locations.lastOrNull() ?: return
                fusedClient.removeLocationUpdates(this)

                viewModelScope.launch {
                    repository.geocodeReverse(
                        latitude = location.latitude,
                        longitude = location.longitude,
                    )
                        .onSuccess("VMl") { city -> repository.addCity(city.copy(locationStatus = LocationStatus.Current)) }
                        .onFailure("VMl") { e -> dispatch(LocationEvent.LoadFailed(e.message, e.cause)) }
                }
            }
        }

        fusedClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper(),
        )
    }
}
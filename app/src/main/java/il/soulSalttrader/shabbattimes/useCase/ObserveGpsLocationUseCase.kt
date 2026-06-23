package il.soulSalttrader.shabbattimes.useCase

import android.location.Location
import il.soulSalttrader.shabbattimes.model.Coordinates
import il.soulSalttrader.shabbattimes.model.CurrentLocationState
import il.soulSalttrader.shabbattimes.model.LocationPermission
import il.soulSalttrader.shabbattimes.repository.GpsLocationRepository
import il.soulSalttrader.shabbattimes.repository.PermissionRepository
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class ObserveGpsLocationUseCase @Inject constructor(
    private val permissionRepository: PermissionRepository,
    private val gpsLocationRepository: GpsLocationRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    val rawLocation: Flow<Location?> = permissionRepository.permissionState
        .flatMapLatest { permission ->
            when (permission) {
                is LocationPermission.Granted -> gpsLocationRepository.location
                else                          -> flowOf(null)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<CurrentLocationState> = permissionRepository.permissionState
        .flatMapLatest { permission ->
            when (permission) {
                is LocationPermission.Granted -> gpsLocationRepository.location
                    .map { location ->
                        if (location != null) CurrentLocationState.Available(
                            coordinates = Coordinates(location.latitude, location.longitude)
                        )
                        else CurrentLocationState.Fetching
                    }
                    .onStart { emit(CurrentLocationState.Fetching) } // ← only when Granted
                else -> flowOf(CurrentLocationState.Idle)            // ← Idle for everything else
            }
        }
}
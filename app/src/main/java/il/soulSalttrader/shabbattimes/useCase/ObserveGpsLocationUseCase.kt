package il.soulSalttrader.shabbattimes.useCase

import android.location.Location
import il.soulSalttrader.shabbattimes.model.LocationPermission
import il.soulSalttrader.shabbattimes.repository.GpsLocationRepository
import il.soulSalttrader.shabbattimes.repository.PermissionRepository
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class ObserveGpsLocationUseCase @Inject constructor(
    private val permissionRepository: PermissionRepository,
    private val gpsLocationRepository: GpsLocationRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Location?> = permissionRepository.permissionState
        .flatMapLatest { permission ->
            when (permission) {
                is LocationPermission.Granted -> gpsLocationRepository.location
                else                          -> flowOf(null)
            }
        }
}
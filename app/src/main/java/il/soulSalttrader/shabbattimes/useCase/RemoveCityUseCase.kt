package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.content.city.CityStatus
import il.soulSalttrader.shabbattimes.location.LocationPermission
import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.repository.CityRepository
import il.soulSalttrader.shabbattimes.repository.PermissionRepository
import javax.inject.Inject

class RemoveCityUseCase @Inject constructor(
    private val cityRepository: CityRepository,
    private val permissionRepository: PermissionRepository,
) {
    suspend operator fun invoke(city: City) {
        cityRepository.removeCity(city)
        if (city.status == CityStatus.Current) {
            permissionRepository.updatePermissionState(LocationPermission.Idle)
        }
    }
}
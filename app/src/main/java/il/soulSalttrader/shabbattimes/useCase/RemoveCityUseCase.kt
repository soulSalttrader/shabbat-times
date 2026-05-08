package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.di.InMemory
import il.soulSalttrader.shabbattimes.model.LocationPermission
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepository
import il.soulSalttrader.shabbattimes.repository.PermissionRepository
import javax.inject.Inject

class RemoveCityUseCase @Inject constructor(
    @param:InMemory private val savedLocationsRepository: SavedLocationsRepository,
    private val permissionRepository: PermissionRepository,
) {
    suspend operator fun invoke(location: SavedLocation, isCurrent: Boolean) {
        savedLocationsRepository.remove(location)
        if (isCurrent) { permissionRepository.updatePermissionState(LocationPermission.Idle) }
    }
}
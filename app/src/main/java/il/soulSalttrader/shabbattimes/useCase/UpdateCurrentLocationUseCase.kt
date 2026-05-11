package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.di.InMemory
import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.repository.CurrentLocationRepository
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepository
import jakarta.inject.Inject

class UpdateCurrentLocationUseCase @Inject constructor(
    @param:InMemory private val currentLocationRepository: CurrentLocationRepository,
    @param:InMemory private val savedLocationsRepository: SavedLocationsRepository,
) {
    suspend operator fun invoke(resolved: ResolvedLocation?) {
        val location = resolved?.let {
            SavedLocation(
                id = SavedLocation.GPS_ID,
                name = it.name,
                coordinates = it.coordinates,
                timeZoneId = it.timeZoneId,
            )
        }
        currentLocationRepository.update(location)
        location?.let { savedLocationsRepository.save(it) }
    }
}
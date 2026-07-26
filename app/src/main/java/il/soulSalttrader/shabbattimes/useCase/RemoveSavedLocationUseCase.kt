package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.di.InMemory
import il.soulSalttrader.shabbattimes.di.Persisted
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.repository.CurrentLocationRepository
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepository
import javax.inject.Inject

class RemoveSavedLocationUseCase @Inject constructor(
    @param:InMemory private val currentLocationRepository: CurrentLocationRepository,
    @param:Persisted private val savedLocationsRepository: SavedLocationsRepository,
) {
    suspend operator fun invoke(location: SavedLocation, isCurrent: Boolean) {
        savedLocationsRepository.remove(location)
        if (isCurrent) { currentLocationRepository.update(null) }
    }
}
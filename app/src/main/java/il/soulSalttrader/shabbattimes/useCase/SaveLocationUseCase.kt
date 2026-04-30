package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepository
import jakarta.inject.Inject

class SaveLocationUseCase @Inject constructor(
    private val savedLocationsRepository: SavedLocationsRepository,
) {
    suspend operator fun invoke(resolved: ResolvedLocation) {
        savedLocationsRepository.save(
            SavedLocation(
                id = resolved.id,
                name = resolved.name,
                coordinates = resolved.coordinates,
                timeZoneId = resolved.timeZoneId,
            )
        )
    }

    suspend operator fun invoke(saved: SavedLocation) {
        savedLocationsRepository.save(saved)
    }
}
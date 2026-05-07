package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.repository.CurrentLocationRepository
import jakarta.inject.Inject

class UpdateCurrentLocationUseCase @Inject constructor(
    private val currentLocationRepository: CurrentLocationRepository,
) {
    suspend operator fun invoke(resolved: ResolvedLocation?) {
        resolved?.let {
            currentLocationRepository.update(
                SavedLocation(
                    id = SavedLocation.GPS_ID,
                    name = resolved.name,
                    coordinates = resolved.coordinates,
                    timeZoneId = resolved.timeZoneId,
                )
            )
        }
    }
}
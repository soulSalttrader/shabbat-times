package il.soulSalttrader.shabbattimes.useCase

import android.location.Location
import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.network.onSuccess
import il.soulSalttrader.shabbattimes.repository.GeocodingRepository
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepository
import jakarta.inject.Inject

class ResolveCurrentCityUseCase @Inject constructor(
    private val geocodingRepository: GeocodingRepository,
    private val savedLocationsRepository: SavedLocationsRepository,
) {
    suspend operator fun invoke(location: Location): NetworkResult<ResolvedLocation> {
        return geocodingRepository.reverseGeocode(location)
            .also { result ->
                result.onSuccess("UpdateCurrentCityUseCase") { loc ->
                    savedLocationsRepository.save(loc)
                }
            }
    }
}
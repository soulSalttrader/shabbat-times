package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.repository.GeocodingRepository
import jakarta.inject.Inject

class GetCitySuggestionsUseCase @Inject constructor(
    private val geocodingRepository: GeocodingRepository,
) {
    suspend operator fun invoke(query: String): NetworkResult<List<ResolvedLocation>> {
        if (query.isEmpty()) return NetworkResult.Success(emptyList())
        return geocodingRepository.autocompleteGeocode(query)
    }
}
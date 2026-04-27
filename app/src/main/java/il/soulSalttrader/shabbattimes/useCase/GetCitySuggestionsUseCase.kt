package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.repository.CityRepository
import jakarta.inject.Inject

class GetCitySuggestionsUseCase @Inject constructor(
    private val cityRepository: CityRepository,
) {
    suspend operator fun invoke(query: String): NetworkResult<List<City>> {
        if (query.isEmpty()) return NetworkResult.Success(emptyList())
        return cityRepository.geocodeAutocomplete(query)
    }
}
package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.location.LocationStatus
import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.network.onSuccess
import il.soulSalttrader.shabbattimes.repository.CityRepository
import javax.inject.Inject

class ResolveCurrentCityUseCase @Inject constructor(
    private val cityRepository: CityRepository,
) {
    suspend operator fun invoke(lat: Double, lng: Double): NetworkResult<City> =
        cityRepository.geocodeReverse(latitude = lat, longitude = lng)
            .also { result ->
                result.onSuccess("UpdateCurrentCityUseCase") { city ->
                    cityRepository.setCurrentCity(city.copy(locationStatus = LocationStatus.Current))
                }
            }
}
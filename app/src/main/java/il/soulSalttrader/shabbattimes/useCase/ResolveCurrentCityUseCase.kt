package il.soulSalttrader.shabbattimes.useCase

import android.location.Location
import il.soulSalttrader.shabbattimes.content.city.CityStatus
import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.network.onSuccess
import il.soulSalttrader.shabbattimes.repository.CityRepository
import javax.inject.Inject

class ResolveCurrentCityUseCase @Inject constructor(
    private val cityRepository: CityRepository,
) {
    suspend operator fun invoke(location: Location): NetworkResult<City> {
        return cityRepository.geocodeReverse(
            latitude = location.latitude,
            longitude = location.longitude,
        )
            .also { result ->
                result.onSuccess("UpdateCurrentCityUseCase") { city ->
                    cityRepository.setCurrentCity(city.copy(status = CityStatus.Current))
                }
            }
    }
}
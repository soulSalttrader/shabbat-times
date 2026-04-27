package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    val cities: Flow<List<City>>
    suspend fun addCity(city: City)
    suspend fun removeCity(city: City)
    suspend fun setCurrentCity(city: City)

    suspend fun geocodeAutocomplete(query: String): NetworkResult<List<City>>
    suspend fun geocodeForward(query: String): NetworkResult<City?>
    suspend fun geocodeReverse(latitude: Double, longitude: Double): NetworkResult<City>
}
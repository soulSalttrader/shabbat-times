package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.model.City
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    val cities: Flow<List<City>>
    suspend fun addCity(city: City)

    suspend fun geocodeAutocomplete(query: String): Flow<List<City>>
    suspend fun geocodeForward(query: String): Flow<City?>
    suspend fun geocodeReverse(latitude: Double, longitude: Double): City?
}
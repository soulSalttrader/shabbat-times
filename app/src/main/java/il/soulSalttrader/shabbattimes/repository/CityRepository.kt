package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.model.City
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    fun searchCities(query: String): Flow<List<City>>
}
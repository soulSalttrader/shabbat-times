package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay
import il.soulSalttrader.shabbattimes.model.SolarTimes
import il.soulSalttrader.shabbattimes.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ShabbatRepository {
    suspend fun getSolarTimes(date: LocalDate, city: City): NetworkResult<SolarTimes>
    suspend fun getHalachicTimes(city: City): NetworkResult<HalachicTimesDisplay>
    suspend fun getHalachicTimesForCities(cities: Flow<List<City>>): Flow<List<NetworkResult<HalachicTimesDisplay>>>
}
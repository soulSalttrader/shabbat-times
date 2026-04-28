package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay
import il.soulSalttrader.shabbattimes.network.NetworkResult

interface ShabbatRepository {
    suspend fun getHalachicTimes(city: City): NetworkResult<HalachicTimesDisplay>
    suspend fun getHalachicTimes(cities: List<City>): List<NetworkResult<HalachicTimesDisplay>>
}
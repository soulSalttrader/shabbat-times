package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay
import il.soulSalttrader.shabbattimes.model.SolarTimes
import il.soulSalttrader.shabbattimes.network.NetworkResult
import java.time.LocalDate

interface ShabbatRepository {
    suspend fun getSolarTimes(date: LocalDate): NetworkResult<SolarTimes>
    suspend fun getHalachicTimes(): NetworkResult<HalachicTimesDisplay>
}
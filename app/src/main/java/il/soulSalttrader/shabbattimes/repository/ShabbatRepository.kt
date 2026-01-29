package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.shabbatApp.model.HalachicTimesDisplay
import il.soulSalttrader.shabbattimes.shabbatApp.model.SolarTimes
import il.soulSalttrader.shabbattimes.shabbatApp.network.NetworkResult
import java.time.LocalDate

interface ShabbatRepository {
    suspend fun getSolarTimes(date: LocalDate): NetworkResult<SolarTimes>
    suspend fun getHalachicTimes(): NetworkResult<HalachicTimesDisplay>
}
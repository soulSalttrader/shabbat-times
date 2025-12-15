package il.soulSalttrader.retro.shabbatApp.repository

import il.soulSalttrader.retro.shabbatApp.model.HalachicTimesDisplay
import il.soulSalttrader.retro.shabbatApp.model.SolarTimes
import il.soulSalttrader.retro.shabbatApp.network.NetworkResult
import java.time.LocalDate

interface ShabbatRepository {
    suspend fun getSolarTimes(date: LocalDate): NetworkResult<SolarTimes>
    suspend fun getHalachicTimes(): NetworkResult<HalachicTimesDisplay>
}
package il.soulSalttrader.retro.shabbatApp.repository

import il.soulSalttrader.retro.shabbatApp.model.HalachicTimes
import il.soulSalttrader.retro.shabbatApp.network.dto.SolarTimes
import il.soulSalttrader.retro.shabbatApp.network.NetworkResult

interface ShabbatRepository {
    suspend fun getSolarTimes(date: String): NetworkResult<SolarTimes>
    suspend fun getHalachicTimes(): NetworkResult<HalachicTimes>
}
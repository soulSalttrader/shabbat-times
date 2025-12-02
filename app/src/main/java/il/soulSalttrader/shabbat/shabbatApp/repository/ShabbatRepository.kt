package il.soulSalttrader.retro.shabbatApp.repository

import il.soulSalttrader.retro.shabbatApp.model.SolarTimes
import il.soulSalttrader.retro.shabbatApp.network.NetworkResult

interface ShabbatRepository {
    suspend fun getSolarTimes(): NetworkResult<SolarTimes>
}
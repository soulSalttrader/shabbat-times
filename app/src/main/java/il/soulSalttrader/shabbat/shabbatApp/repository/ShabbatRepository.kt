package il.soulSalttrader.retro.shabbatApp.repository

import il.soulSalttrader.retro.shabbatApp.model.Shabbat
import il.soulSalttrader.retro.shabbatApp.network.NetworkResult

interface ShabbatRepository {
    suspend fun getShabbatTimes(): NetworkResult<Shabbat>
}
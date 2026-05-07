package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.model.SolarTimes
import il.soulSalttrader.shabbattimes.model.SolarTimesRequest
import il.soulSalttrader.shabbattimes.network.NetworkResult

interface SolarTimesRepository {
    suspend fun getSolarTimes(request: SolarTimesRequest): NetworkResult<SolarTimes>
}
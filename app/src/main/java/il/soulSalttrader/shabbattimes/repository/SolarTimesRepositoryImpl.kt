package il.soulSalttrader.shabbattimes.repository

import android.util.Log
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.common.asNetworkFailure
import il.soulSalttrader.shabbattimes.common.toDisplayString
import il.soulSalttrader.shabbattimes.di.SolarTimesService
import il.soulSalttrader.shabbattimes.model.SolarTimesRequest
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.network.dto.asNetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SolarTimesRepositoryImpl @Inject constructor(
    private val apiService: SolarTimesService,
    private val dispatcher: CoroutineDispatcher,
) : SolarTimesRepository {

    override suspend fun getSolarTimes(request: SolarTimesRequest) =
        withContext(context = dispatcher) {
            val tag = "ShabbatRepositoryImpl.getSolarTimes"
            runCatching {
                apiService.api.getSolarTimes(
                    date = request.date.toDisplayString(),
                    lat = request.coordinates.latitude,
                    lng = request.coordinates.longitude,
                    timezone = request.timeZone.id,
                    timeFormat = 24,
                )
            }
                .map { dto ->
                    if (Debug.enabled) Log.d(tag, dto.status)
                    dto.asNetworkResult()
                }
                .recover { exception ->
                    if (Debug.enabled) Log.d(tag, "API call failed", exception)
                    exception.asNetworkFailure()
                }
                .getOrElse { NetworkResult.Failure("Critical failure") }
        }
}
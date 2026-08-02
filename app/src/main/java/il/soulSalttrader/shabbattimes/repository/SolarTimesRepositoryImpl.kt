package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.common.asNetworkFailure
import il.soulSalttrader.shabbattimes.common.toApiDateString
import il.soulSalttrader.shabbattimes.di.SolarTimesService
import il.soulSalttrader.shabbattimes.model.SolarTimesRequest
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.network.dto.asNetworkResult
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@Singleton
class SolarTimesRepositoryImpl @Inject constructor(
    private val apiService: SolarTimesService,
    private val dispatcher: CoroutineDispatcher,
) : SolarTimesRepository {

    override suspend fun getSolarTimes(request: SolarTimesRequest) =
        withContext(context = dispatcher) {
            runCatching {
                apiService.api.getSolarTimes(
                    date = request.date.toApiDateString(),
                    lat = request.coordinates.latitude,
                    lng = request.coordinates.longitude,
                    timezone = request.timeZone.id,
                    timeFormat = 24,
                )
            }
                .map { dto ->
                    dto.asNetworkResult()
                }
                .recover { cause ->
                    cause.asNetworkFailure()
                }
                .getOrElse { cause -> NetworkResult.Failure(cause) }
        }
}

package il.soulSalttrader.shabbattimes.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.common.getOrElse
import il.soulSalttrader.shabbattimes.common.toDisplayString
import il.soulSalttrader.shabbattimes.common.upcomingCandleLightingDate
import il.soulSalttrader.shabbattimes.common.upcomingHavdalahDate
import il.soulSalttrader.shabbattimes.constants.ShabbatOffsets.HILUCH_MIL_MINUTES
import il.soulSalttrader.shabbattimes.constants.ShabbatOffsets.TZEIT_HAKOCHAVIM_MINUTES
import il.soulSalttrader.shabbattimes.model.HalachicTimes
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay
import il.soulSalttrader.shabbattimes.model.SolarTimes
import il.soulSalttrader.shabbattimes.model.toDisplay
import il.soulSalttrader.shabbattimes.model.toDomain
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.network.ShabbatAPIService
import il.soulSalttrader.shabbattimes.settings.UserPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShabbatRepositoryImpl @Inject constructor(
    private val apiService: ShabbatAPIService,
    private val dispatcher: CoroutineDispatcher,
    private val userPreferences: UserPreferences,
    @param:ApplicationContext private val context: Context,
) : ShabbatRepository {

    override suspend fun getSolarTimes(date: LocalDate): NetworkResult<SolarTimes> = withContext(context = dispatcher) {
        runCatching {
            apiService.getSolarTimes(date = date.toDisplayString())
        }
            .map { dto ->
                if (Debug.enabled) Log.d("ShabbatRepositoryImpl.getSolarTimes", dto.status)

                when (dto.status.uppercase()) {
                    "OK" -> NetworkResult.Success(data = dto.results.toDomain(userPreferences.is24HourFormat()))
                    else -> NetworkResult.Failure(message = dto.status)
                }
            }
            .recover { exception ->
                val message = when (exception) {
                    is HttpException -> "HTTP ${exception.code()}"
                    is IOException   -> "No internet connection. ${exception.message}."
                    else             -> exception.message ?: "Unexpected error"
                }

                if (Debug.enabled) Log.d("ShabbatRepositoryImpl.getSolarTimes", message)

                NetworkResult.Failure(message = message, cause = exception)
            }
            .getOrThrow()
    }

    override suspend fun getHalachicTimes(): NetworkResult<HalachicTimesDisplay> = withContext(dispatcher) {
        val friday = upcomingCandleLightingDate()
        val saturday = upcomingHavdalahDate()

        val (fridaySolar, saturdaySolar) = awaitAll(
            async { getSolarTimes(friday) },
            async { getSolarTimes(saturday) }
        ).map { it.getOrElse { failure -> return@withContext failure } }

        NetworkResult.Success(
            HalachicTimes(
                candleLightingTime = fridaySolar.sunset.minusMinutes(HILUCH_MIL_MINUTES),
                candleLightingDate = friday,
                havdalahTime = saturdaySolar.sunset.plusMinutes(TZEIT_HAKOCHAVIM_MINUTES),
                havdalahDate = saturday
            ).toDisplay(context)
        )
    }
}
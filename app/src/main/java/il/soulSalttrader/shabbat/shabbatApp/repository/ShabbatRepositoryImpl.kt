package il.soulSalttrader.retro.shabbatApp.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.shabbatApp.common.getOrElse
import il.soulSalttrader.retro.shabbatApp.common.toDisplayString
import il.soulSalttrader.retro.shabbatApp.common.upcomingCandleLightingDate
import il.soulSalttrader.retro.shabbatApp.common.upcomingHavdalahDate
import il.soulSalttrader.retro.shabbatApp.constants.ShabbatOffsets.HILUCH_MIL_MINUTES
import il.soulSalttrader.retro.shabbatApp.constants.ShabbatOffsets.TZEIT_HAKOCHAVIM_MINUTES
import il.soulSalttrader.retro.shabbatApp.model.HalachicTimes
import il.soulSalttrader.retro.shabbatApp.model.HalachicTimesDisplay
import il.soulSalttrader.retro.shabbatApp.model.SolarTimes
import il.soulSalttrader.retro.shabbatApp.model.toDisplay
import il.soulSalttrader.retro.shabbatApp.model.toDomain
import il.soulSalttrader.retro.shabbatApp.network.NetworkResult
import il.soulSalttrader.retro.shabbatApp.network.ShabbatAPIService
import il.soulSalttrader.retro.shabbatApp.settings.UserPreferences
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
                havdalahTime       = saturdaySolar.sunset.plusMinutes(TZEIT_HAKOCHAVIM_MINUTES),
                havdalahDate       = saturday
            ).toDisplay(context)
        )
    }
}
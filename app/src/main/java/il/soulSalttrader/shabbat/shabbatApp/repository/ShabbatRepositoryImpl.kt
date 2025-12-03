package il.soulSalttrader.retro.shabbatApp.repository

import android.util.Log
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.shabbatApp.common.toDisplayString
import il.soulSalttrader.retro.shabbatApp.common.upcomingCandleLightingDate
import il.soulSalttrader.retro.shabbatApp.common.upcomingHavdalahDate
import il.soulSalttrader.retro.shabbatApp.constants.ShabbatOffsets
import il.soulSalttrader.retro.shabbatApp.model.HalachicTimes
import il.soulSalttrader.retro.shabbatApp.model.SolarTimes
import il.soulSalttrader.retro.shabbatApp.model.toDomain
import il.soulSalttrader.retro.shabbatApp.network.NetworkResult
import il.soulSalttrader.retro.shabbatApp.network.ShabbatAPIService
import il.soulSalttrader.retro.shabbatApp.settings.UserPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShabbatRepositoryImpl @Inject constructor(
    private val apiService: ShabbatAPIService,
    private val dispatcher: CoroutineDispatcher,
    private val userPreferences: UserPreferences,
) : ShabbatRepository {

    override suspend fun getSolarTimes(date: String): NetworkResult<SolarTimes> = withContext(context = dispatcher) {
        runCatching {
            apiService.getSolarTimes(date = date)
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

    override suspend fun getHalachicTimes(): NetworkResult<HalachicTimes> = withContext(dispatcher) {
        coroutineScope {
            val upcomingCandleLightingDate = upcomingCandleLightingDate()
            val upcomingHavdalahDate = upcomingHavdalahDate()

            val candleLightingDeferred = async { getSolarTimes(date = upcomingCandleLightingDate.toDisplayString()) }
            val havdalahDeferred = async { getSolarTimes(date = upcomingHavdalahDate.toDisplayString()) }

            val candleLightingResult = candleLightingDeferred.await()
            val havdalahResult = havdalahDeferred.await()

            if (candleLightingResult is NetworkResult.Failure) return@coroutineScope candleLightingResult
            if (havdalahResult is NetworkResult.Failure) return@coroutineScope havdalahResult

            val candleLightingData = (candleLightingResult as NetworkResult.Success).data
            val havdalahData = (havdalahResult as NetworkResult.Success).data

            val candleLightingTime = candleLightingData.sunset.minusMinutes(ShabbatOffsets.HILUCH_MIL_MINUTES)
            val havdalahTime = havdalahData.sunset.plusMinutes(ShabbatOffsets.TZEIT_HAKOCHAVIM_MINUTES)

            NetworkResult.Success(
                data = HalachicTimes(
                    candleLightingTime = candleLightingTime,
                    candleLightingDate = candleLightingData.date,
                    havdalahTime = havdalahTime,
                    havdalahDate = havdalahData.date,
                )
            )
        }
    }
}
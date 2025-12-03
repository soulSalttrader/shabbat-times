package il.soulSalttrader.retro.shabbatApp.playground.mvvm

import android.util.Log
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.shabbatApp.common.getOrElse
import il.soulSalttrader.retro.shabbatApp.common.toDisplayString
import il.soulSalttrader.retro.shabbatApp.common.upcomingCandleLightingDate
import il.soulSalttrader.retro.shabbatApp.common.upcomingHavdalahDate
import il.soulSalttrader.retro.shabbatApp.constants.ShabbatOffsets.HILUCH_MIL_MINUTES
import il.soulSalttrader.retro.shabbatApp.constants.ShabbatOffsets.TZEIT_HAKOCHAVIM_MINUTES
import il.soulSalttrader.retro.shabbatApp.model.HalachicTimes
import il.soulSalttrader.retro.shabbatApp.model.SolarTimes
import il.soulSalttrader.retro.shabbatApp.model.toDomain
import il.soulSalttrader.retro.shabbatApp.network.NetworkResult
import il.soulSalttrader.retro.shabbatApp.network.ShabbatAPIService
import il.soulSalttrader.retro.shabbatApp.repository.ShabbatRepository
import il.soulSalttrader.retro.shabbatApp.settings.UserPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.component1
import kotlin.collections.component2

@Singleton
class ShabbatRepositoryImplMVVM @Inject constructor(
    private val apiService: ShabbatAPIService,
    private val dispatcher: CoroutineDispatcher,
    private val userPreferences: UserPreferences,
) : ShabbatRepository {

    override suspend fun getSolarTimes(date: String): NetworkResult<SolarTimes> = withContext(dispatcher) {
        try {
            val dto = apiService.getSolarTimes(date = date)
            if (Debug.enabled) Log.d("ShabbatRepositoryImplMVVM.getSolarTimes", dto.status)

            when (dto.status.uppercase()) {
                "OK" -> NetworkResult.Success(dto.results.toDomain(userPreferences.is24HourFormat()))
                else -> NetworkResult.Failure(dto.status)
            }

        } catch (e: HttpException) {
            if (Debug.enabled) Log.d("ShabbatRepositoryImplMVVM.getSolarTimes", e.message ?: "HttpException")
            NetworkResult.Failure(e.message ?: "Http error", e.cause)
        } catch (e: IOException) {
            if (Debug.enabled) Log.d("ShabbatRepositoryImplMVVM.getSolarTimes", e.message ?: "IOException")
            NetworkResult.Failure(e.message ?: "No Internet Connection", e.cause)
        } catch (e: Exception) {
            if (Debug.enabled) Log.d("ShabbatRepositoryImplMVVM.getSolarTimes", e.message ?: "Exception")
            NetworkResult.Failure(e.message ?: "Unexpected error", e.cause)
        }
    }

    override suspend fun getHalachicTimes(): NetworkResult<HalachicTimes> = withContext(dispatcher) {
        val friday = upcomingCandleLightingDate()
        val saturday = upcomingHavdalahDate()

        val (fridaySolar, saturdaySolar) = awaitAll(
            async { getSolarTimes(friday.toDisplayString()) },
            async { getSolarTimes(saturday.toDisplayString()) }
        ).map { it.getOrElse { failure -> return@withContext failure } }

        NetworkResult.Success(
            HalachicTimes(
                candleLightingTime = fridaySolar.sunset.minusMinutes(HILUCH_MIL_MINUTES),
                candleLightingDate = friday,
                havdalahTime       = saturdaySolar.sunset.plusMinutes(TZEIT_HAKOCHAVIM_MINUTES),
                havdalahDate       = saturday
            )
        )
    }
}
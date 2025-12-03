package il.soulSalttrader.retro.shabbatApp.repository

import android.util.Log
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.shabbatApp.model.HalachicTimes
import il.soulSalttrader.retro.shabbatApp.model.SolarTimes
import il.soulSalttrader.retro.shabbatApp.model.toDomain
import il.soulSalttrader.retro.shabbatApp.network.NetworkResult
import il.soulSalttrader.retro.shabbatApp.network.ShabbatAPIService
import il.soulSalttrader.retro.shabbatApp.settings.UserPreferences
import kotlinx.coroutines.CoroutineDispatcher
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

    override suspend fun getHalachicTimes(): NetworkResult<HalachicTimes> {
        TODO("Not yet implemented")
    }
}
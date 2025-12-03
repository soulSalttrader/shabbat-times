package il.soulSalttrader.retro.shabbatApp.playground.mvvm

import android.util.Log
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.shabbatApp.model.HalachicTimes
import il.soulSalttrader.retro.shabbatApp.model.SolarTimes
import il.soulSalttrader.retro.shabbatApp.network.NetworkResult
import il.soulSalttrader.retro.shabbatApp.network.ShabbatAPIService
import il.soulSalttrader.retro.shabbatApp.repository.ShabbatRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShabbatRepositoryImplMVVM @Inject constructor(
    private val apiService: ShabbatAPIService,
    private val dispatcher: CoroutineDispatcher,
) : ShabbatRepository {

    override suspend fun getSolarTimes(date: String): NetworkResult<SolarTimes> = withContext(dispatcher) {
        try {
            val dto = apiService.getSolarTimes(date = date)
            if (Debug.enabled) Log.d("ShabbatRepositoryImplMVVM.getSolarTimes", dto.status)

            when (dto.status.uppercase()) {
                "OK" -> NetworkResult.Success(dto.results)
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

    override suspend fun getHalachicTimes(): NetworkResult<HalachicTimes> {
        TODO("Not yet implemented")
    }
}
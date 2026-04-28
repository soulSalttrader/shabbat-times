package il.soulSalttrader.shabbattimes.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.common.asNetworkFailure
import il.soulSalttrader.shabbattimes.common.toDisplayString
import il.soulSalttrader.shabbattimes.common.upcomingCandleLightingDate
import il.soulSalttrader.shabbattimes.common.upcomingHavdalahDate
import il.soulSalttrader.shabbattimes.di.SolarTimesService
import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.model.HalachicTimes
import il.soulSalttrader.shabbattimes.model.toDisplay
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.network.dto.asNetworkResult
import il.soulSalttrader.shabbattimes.network.getOrThrow
import il.soulSalttrader.shabbattimes.settings.UserPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShabbatRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val context: Context,
) : ShabbatRepository {
    override suspend fun getHalachicTimes(city: City) = withContext(dispatcher) {
        val friday = upcomingCandleLightingDate()
        val saturday = upcomingHavdalahDate()

        val candleLightingOffsetMinutes = 18L
        val havdalahOffsetMinutes = 40L

        runCatching {
            awaitAll(
                async { getSolarTimes(friday, city) },
                async { getSolarTimes(saturday, city) }
            ).map { it.getOrThrow() }
        }.fold(
            onSuccess = { (fridaySolar, saturdaySolar) ->
                NetworkResult.Success(
                    HalachicTimes(
                        city = city,
                        candleLightingTime = fridaySolar.sunset.minusMinutes(candleLightingOffsetMinutes),
                        candleLightingDate = friday,
                        havdalahTime = saturdaySolar.sunset.plusMinutes(havdalahOffsetMinutes),
                        havdalahDate = saturday,
                    ).toDisplay(context)
                )
            },
            onFailure = { e ->
                NetworkResult.Failure(
                    message = "Failed to calculate Shabbat times for ${city.name}",
                    cause = e
                )
            }
        )
    }

    override suspend fun getHalachicTimes(cities: List<City>) = coroutineScope {
        cities.map { city ->
            async(dispatcher) {
                getHalachicTimes(city)
            }
        }.awaitAll()
    }
}
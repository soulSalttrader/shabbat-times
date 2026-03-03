package il.soulSalttrader.shabbattimes.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.common.asNetworkFailure
import il.soulSalttrader.shabbattimes.common.getOrThrow
import il.soulSalttrader.shabbattimes.common.toDisplayString
import il.soulSalttrader.shabbattimes.common.upcomingCandleLightingDate
import il.soulSalttrader.shabbattimes.common.upcomingHavdalahDate
import il.soulSalttrader.shabbattimes.di.SolarTimesService
import il.soulSalttrader.shabbattimes.location.LocationStatus
import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.model.HalachicTimes
import il.soulSalttrader.shabbattimes.repository.SeedCities.BRNO
import il.soulSalttrader.shabbattimes.repository.SeedCities.JERUSALEM
import il.soulSalttrader.shabbattimes.repository.SeedCities.NEW_YORK
import il.soulSalttrader.shabbattimes.model.toDisplay
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.network.dto.asNetworkResult
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
    private val apiService: SolarTimesService,
    private val dispatcher: CoroutineDispatcher,
    private val userPreferences: UserPreferences,
    @param:ApplicationContext private val context: Context,
) : ShabbatRepository {

    override suspend fun getSolarTimes(date: LocalDate, city: City) = withContext(context = dispatcher) {
        val tag = "ShabbatRepositoryImpl.getSolarTimes"
        runCatching {
            apiService.api.getSolarTimes(
                date = date.toDisplayString(),
                lat = city.coordinates.latitude,
                lng = city.coordinates.longitude,
                timezone = city.timeZone.id,
            )
        }
            .map { dto ->
                if (Debug.enabled) Log.d(tag, dto.status)
                dto.asNetworkResult(use24HourFormat = userPreferences.is24HourFormat())
            }
            .recover { exception ->
                if (Debug.enabled) Log.d(tag, "API call failed", exception)
                exception.asNetworkFailure()
            }
            .getOrElse { NetworkResult.Failure("Critical failure") }
    }

    override suspend fun getHalachicTimes(city: City) = withContext(dispatcher) {
        val friday = upcomingCandleLightingDate()
        val saturday = upcomingHavdalahDate()

        val candleLightingOffsetMinutes = 18L
        val havdalahOffsetMinutes = 40L

        val distanceJerusalemToNycKm = 9195
        val distanceJerusalemToBrnoKm = 2319

        val locationStatus = when (city) {
            JERUSALEM -> LocationStatus.Current
            NEW_YORK -> LocationStatus.Distance(distanceJerusalemToNycKm)
            BRNO -> LocationStatus.Distance(distanceJerusalemToBrnoKm)
            else -> LocationStatus.Unknown
        }

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
                        locationStatus = locationStatus,
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
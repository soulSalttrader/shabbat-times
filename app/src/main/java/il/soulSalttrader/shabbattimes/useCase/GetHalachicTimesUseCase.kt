package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.model.HalachicTimes
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.model.ShabbatCalendar
import il.soulSalttrader.shabbattimes.model.SolarTimesRequest
import il.soulSalttrader.shabbattimes.model.resolveCandleLighting
import il.soulSalttrader.shabbattimes.model.resolveHavdalah
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.network.getOrThrow
import il.soulSalttrader.shabbattimes.repository.SolarTimesRepository
import il.soulSalttrader.shabbattimes.settings.ShabbatPreferences
import il.soulSalttrader.shabbattimes.settings.solarPosition.SolarDepressionTimeCalculator
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class GetHalachicTimesUseCase @Inject constructor(
    private val solarTimesRepository: SolarTimesRepository,
    private val shabbatCalendar: ShabbatCalendar,
    private val havdalahCalculator: SolarDepressionTimeCalculator,
) {
    suspend operator fun invoke(
        locations: List<SavedLocation>,
        preferences: ShabbatPreferences,
    ): List<NetworkResult<HalachicTimes>> =
        coroutineScope {
            locations.map { location ->
                async {
                    getHalachicTimes(
                        preferences = preferences,
                        startEvent = SolarTimesRequest(
                            date = shabbatCalendar.upcomingCandleLightingDate(),
                            coordinates = location.coordinates,
                            timeZone = location.timeZoneId,
                        ),
                        endEvent = SolarTimesRequest(
                            date = shabbatCalendar.upcomingHavdalahDate(),
                            coordinates = location.coordinates,
                            timeZone = location.timeZoneId,
                        ),
                    )
                }
            }.awaitAll()
        }

    private suspend fun getHalachicTimes(
        preferences: ShabbatPreferences,
        startEvent: SolarTimesRequest,
        endEvent: SolarTimesRequest,
    ): NetworkResult<HalachicTimes> = runCatching {
        coroutineScope {
            awaitAll(
                async { solarTimesRepository.getSolarTimes(startEvent) },
                async { solarTimesRepository.getSolarTimes(endEvent) },
            ).map { it.getOrThrow() }
        }
    }.fold(
        onSuccess = { (startSolar, endSolar) ->
            val candleLightingState = startSolar.resolveCandleLighting(preferences)
            val havdalahState = endSolar.resolveHavdalah(preferences, startEvent, havdalahCalculator)

            NetworkResult.Success(
                HalachicTimes(
                    coordinates = startEvent.coordinates,
                    candleLighting = candleLightingState,
                    havdalah = havdalahState,
                ),
            )
        },
        onFailure = { cause -> NetworkResult.Failure(cause) },
    )
}

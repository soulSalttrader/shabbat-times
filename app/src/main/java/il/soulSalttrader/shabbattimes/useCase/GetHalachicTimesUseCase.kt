package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.model.HalachicTimes
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.model.ShabbatCalendar
import il.soulSalttrader.shabbattimes.model.SolarTimesRequest
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.network.getOrThrow
import il.soulSalttrader.shabbattimes.repository.SolarTimesRepository
import il.soulSalttrader.shabbattimes.settings.UserPreferences
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class GetHalachicTimesUseCase @Inject constructor(
    private val solarTimesRepository: SolarTimesRepository,
    private val userPreferences: UserPreferences,
    private val shabbatCalendar: ShabbatCalendar,
) {
    suspend operator fun invoke(locations: List<SavedLocation>): List<NetworkResult<HalachicTimes>> =
        coroutineScope {
            locations.map { location ->
                async {
                    getHalachicTimes(
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
        startEvent: SolarTimesRequest,
        endEvent: SolarTimesRequest,
    ): NetworkResult<HalachicTimes> {
        return runCatching {
            coroutineScope {
                awaitAll(
        async { solarTimesRepository.getSolarTimes(startEvent) },
                    async { solarTimesRepository.getSolarTimes(endEvent) },
                ).map { it.getOrThrow() }
            }
        }.fold(
            onSuccess = { (startSolar, endSolar) ->
                NetworkResult.Success(
                    HalachicTimes(
                        coordinates = startEvent.coordinates,
                        candleLightingTime = startSolar.sunset.minusMinutes(userPreferences.candleLightingOffsetMinutes()),
                        candleLightingDate = startEvent.date,
                        havdalahTime = endSolar.sunset.plusMinutes(userPreferences.havdalahOffsetMinutes()),
                        havdalahDate = endEvent.date,
                    )
                )
            },
            onFailure = { e ->
                NetworkResult.Failure(
                    message = "Failed to calculate Shabbat times for ${startEvent.coordinates}",
                    cause = e,
                )
            }
        )
    }
}
package il.soulSalttrader.shabbattimes.settings.solarPosition

import il.soulSalttrader.shabbattimes.model.SolarTimes
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.time.Instant

@Singleton
class AstronomicalSolarDepressionCalculator @Inject constructor(
    private val positionCalculator: SolarPositionCalculator,
    private val altitudeSearch: SolarAltitudeSearch,
) : SolarDepressionTimeCalculator {

    override fun eveningTimeForDepression(
        targetDegrees: Double,
        solarData: SolarTimes,
        latitude: Double,
        longitude: Double,
    ): Instant? {
        val sunset = solarData.sunset ?: return null
        return altitudeSearch.findCrossing(
            from = sunset,
            targetAltitudeDeg = -targetDegrees,
            positionCalculator = positionCalculator,
            latitudeDeg = latitude,
            longitudeDeg = longitude,
        )
    }
}

package il.soulSalttrader.shabbattimes.settings.solarPosition

import il.soulSalttrader.shabbattimes.model.SolarTimes
import java.time.Instant

interface SolarDepressionTimeCalculator {
    fun eveningTimeForDepression(
        targetDegrees: Double,
        solarData: SolarTimes,
        latitude: Double,
        longitude: Double,
    ): Instant?
}
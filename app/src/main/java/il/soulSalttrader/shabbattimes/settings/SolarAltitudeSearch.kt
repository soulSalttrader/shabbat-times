package il.soulSalttrader.shabbattimes.settings

import il.soulSalttrader.shabbattimes.settings.solarPosition.SolarPositionCalculator
import java.time.Instant

interface SolarAltitudeSearch {
    /** Finds the first instant after [from] where altitude crosses [targetAltitudeDeg] (descending). */
    fun findCrossing(
        from: Instant,
        targetAltitudeDeg: Double,
        positionCalculator: SolarPositionCalculator,
        latitudeDeg: Double,
        longitudeDeg: Double,
    ): Instant
}
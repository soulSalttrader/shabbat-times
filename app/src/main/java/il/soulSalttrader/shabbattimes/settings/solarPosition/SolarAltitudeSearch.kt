package il.soulSalttrader.shabbattimes.settings.solarPosition

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
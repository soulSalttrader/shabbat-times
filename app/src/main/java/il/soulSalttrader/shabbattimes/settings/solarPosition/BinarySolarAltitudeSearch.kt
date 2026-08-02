package il.soulSalttrader.shabbattimes.settings.solarPosition

import jakarta.inject.Singleton
import java.time.Duration
import java.time.Instant

@Singleton
class BinarySolarAltitudeSearch : SolarAltitudeSearch {

    companion object {
        private const val SEARCH_WINDOW_HOURS = 6L
        private const val COARSE_STEP_MINUTES = 10L
        private const val SECONDS_PER_HOUR = 3600L
        private const val SECONDS_PER_MINUTE = 60L
        private const val MAX_ITERATIONS = 40 // 6h / 2^40 ≈ sub-microsecond precision
    }

    override fun findCrossing(
        from: Instant,
        targetAltitudeDeg: Double,
        positionCalculator: SolarPositionCalculator,
        latitudeDeg: Double,
        longitudeDeg: Double,
    ): Instant? {
        val altitudeAt = { t: Instant -> positionCalculator.altitudeDegrees(t, latitudeDeg, longitudeDeg) }

        // Scan forward in coarse steps to find a bracket where altitude crosses the target
        val bracket = findBracket(from, targetAltitudeDeg, altitudeAt) ?: return null

        // Binary search within the bracket for sub-second precision
        return binarySearch(bracket, targetAltitudeDeg, altitudeAt, bracket.descending)
    }

    private data class AltitudeBracket(
        val lo: Instant,
        val hi: Instant,
        val descending: Boolean,
    )

    private fun findBracket(
        from: Instant,
        targetAltitudeDeg: Double,
        altitudeAt: (Instant) -> Double,
    ): AltitudeBracket? {
        val totalSeconds = SEARCH_WINDOW_HOURS * SECONDS_PER_HOUR
        val stepSeconds = COARSE_STEP_MINUTES * SECONDS_PER_MINUTE

        var t1 = from
        var alt1 = altitudeAt(t1)

        var offset = stepSeconds
        while (offset <= totalSeconds) {
            val t2 = from.plusSeconds(offset)
            val alt2 = altitudeAt(t2)

            when {
                targetAltitudeDeg in alt2..<alt1 ->
                    return AltitudeBracket(t1, t2, descending = true)
                alt1 < targetAltitudeDeg && alt2 >= targetAltitudeDeg ->
                    return AltitudeBracket(t1, t2, descending = false)
            }

            t1 = t2
            alt1 = alt2
            offset += stepSeconds
        }

        return null // target altitude never reached (e.g. White Nights at high latitudes)
    }

    private fun binarySearch(
        bracket: AltitudeBracket,
        targetAltitudeDeg: Double,
        altitudeAt: (Instant) -> Double,
        descending: Boolean,
    ): Instant {
        var lo = bracket.lo
        var hi = bracket.hi

        repeat(MAX_ITERATIONS) {
            val mid = lo.plusSeconds(Duration.between(lo, hi).seconds / 2)
            val midAltitude = altitudeAt(mid)

            val midIsBeforeTarget = if (descending) {
                midAltitude > targetAltitudeDeg
            } else {
                midAltitude < targetAltitudeDeg
            }

            if (midIsBeforeTarget) lo = mid else hi = mid
        }

        return hi
    }
}

package il.soulSalttrader.shabbattimes.settings

import il.soulSalttrader.shabbattimes.model.SolarTimes
import java.time.Instant

sealed interface SolarTestCase {
    data class Ephemeris(
        val name: String,
        val instant: Instant,
        val expectedDeclinationDeg: Double,
        val expectedEquationOfTimeMinutes: Double,
        val declinationTolerance: Double = 0.08,
        val equationOfTimeTolerance: Double = 0.6,
    ) : SolarTestCase

    data class Observation(
        val name: String,
        val instant: Instant,
        val latitude: Double,
        val longitude: Double,
        val expectedAltitudeRange: ClosedFloatingPointRange<Double>,
        val altitudeTolerance: Double = 0.5,
    ) : SolarTestCase

    data class DepressionTestCase(
        val name: String,
        val solarData: SolarTimes,
        val targetDegrees: Double,
        val latitude: Double,
        val longitude: Double,
        val expectedResult: ClosedRange<Instant>? = null,
    ) : SolarTestCase

    data class SolarAltitudeSearchTestCase(
        val name: String,
        val from: Instant,
        val targetAltitudeDeg: Double,
        val latitude: Double,
        val longitude: Double,
        val expectedWindow: ClosedRange<Instant>? = null,
        val description: String = name,
    ) : SolarTestCase
}

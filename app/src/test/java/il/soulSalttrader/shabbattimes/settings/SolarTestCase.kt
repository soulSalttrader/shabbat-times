package il.soulSalttrader.shabbattimes.settings

import java.time.Instant

sealed class SolarTestCase(
    open val name: String,
    open val instant: Instant
) {

    data class Ephemeris(
        override val name: String,
        override val instant: Instant,
        val expectedDeclinationDeg: Double,
        val expectedEquationOfTimeMinutes: Double,
        val declinationTolerance: Double = 0.08,
        val equationOfTimeTolerance: Double = 0.6
    ) : SolarTestCase(name, instant)

    data class Observation(
        override val name: String,
        override val instant: Instant,
        val latitude: Double,
        val longitude: Double,
        val expectedAltitudeRange: ClosedFloatingPointRange<Double>,
        val altitudeTolerance: Double = 0.5
    ) : SolarTestCase(name, instant)
}
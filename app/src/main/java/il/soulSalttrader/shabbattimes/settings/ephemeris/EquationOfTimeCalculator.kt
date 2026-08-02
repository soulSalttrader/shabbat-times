package il.soulSalttrader.shabbattimes.settings.ephemeris

import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.EQUATION_OF_TIME_MINUTES_PER_DEGREE
import java.lang.Math.toDegrees
import java.lang.Math.toRadians
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.tan

object EquationOfTimeCalculator {

    /**
     * Calculates the Equation of Time in minutes.
     * The Equation of Time is the difference between apparent solar time and mean solar time.
     * Based on Jean Meeus' Astronomical Algorithms.
     */
    fun getEquationOfTimeMinutes(
        meanLongitudeDeg: Double,
        meanAnomalyRad: Double,
        eccentricity: Double,
        correctedObliquityDeg: Double,
    ): Double {
        val l = toRadians(meanLongitudeDeg)

        // y = tan²(ε/2) — standard auxiliary variable accounting for the obliquity of the ecliptic
        val y = getTanHalfObliquitySquared(correctedObliquityDeg)

        val eqTimeRad =
            y * sin(2 * l) -
                2 * eccentricity * sin(meanAnomalyRad) +
                4 * eccentricity * y * sin(meanAnomalyRad) * cos(2 * l) -
                0.5 * y.pow(2) * sin(4 * l) -
                1.25 * eccentricity.pow(2) * sin(2 * meanAnomalyRad)

        return EQUATION_OF_TIME_MINUTES_PER_DEGREE * toDegrees(eqTimeRad)
    }

    /**
     * Calculates the Sun's declination (angle from celestial equator).
     * Formula: δ = arcsin(sin(ε) * sin(λ))
     * where ε = corrected obliquity, λ = apparent longitude of the Sun.
     */
    fun getDeclinationRad(
        correctedObliquityDeg: Double,
        sunApparentLongitudeDeg: Double,
    ): Double =
        asin(sin(toRadians(correctedObliquityDeg)) * sin(toRadians(sunApparentLongitudeDeg)))

    /**
     * Returns y = tan²(ε/2), where ε is the corrected obliquity of the ecliptic.
     * This is a common auxiliary term in solar calculations (used in Jean Meeus' Astronomical Algorithms).
     * It accounts for the projection effect caused by Earth's axial tilt.
     */
    private fun getTanHalfObliquitySquared(
        correctedObliquityDeg: Double,
    ): Double = tan(toRadians(correctedObliquityDeg / 2.0)).pow(2)
}

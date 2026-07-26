package il.soulSalttrader.shabbattimes.settings.ephemeris

import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.APPARENT_LONGITUDE_ABERRATION
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.APPARENT_LONGITUDE_NUTATION_AMPLITUDE
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.CENTER_CORRECTION_C1_BASE
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.CENTER_CORRECTION_C1_PER_CENTURY
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.CENTER_CORRECTION_C1_PER_CENTURY_SQ
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.CENTER_CORRECTION_C2_BASE
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.CENTER_CORRECTION_C2_PER_CENTURY
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.CENTER_CORRECTION_C3
import java.lang.Math.toRadians
import kotlin.math.sin

object SunPositionCalculator {
    /**
     * Equation of the center — correction from mean to true anomaly of the Sun.
     * Based on Jean Meeus' Astronomical Algorithms.
     */
    fun getEquationOfCenterDeg(
        meanAnomalyRad: Double,
        julianCenturiesSinceJ2000: Double,
    ): Double {
        val c1 = CENTER_CORRECTION_C1_BASE - julianCenturiesSinceJ2000 * (CENTER_CORRECTION_C1_PER_CENTURY + CENTER_CORRECTION_C1_PER_CENTURY_SQ * julianCenturiesSinceJ2000)
        val c2 = CENTER_CORRECTION_C2_BASE - CENTER_CORRECTION_C2_PER_CENTURY * julianCenturiesSinceJ2000

        return sin(meanAnomalyRad) * c1 +
                sin(2 * meanAnomalyRad) * c2 +
                sin(3 * meanAnomalyRad) * CENTER_CORRECTION_C3
    }

    fun getSunTrueLongitudeDeg(
        meanLongitudeDeg: Double,
        equationOfCenterDeg: Double,
    ): Double = meanLongitudeDeg + equationOfCenterDeg

    /**
     * Applies nutation and aberration corrections to get the Sun's apparent geocentric longitude.
     */
    fun getSunApparentLongitudeDeg(
        sunTrueLongitudeDeg: Double,
        moonNodeLongitudeDeg: Double,
    ): Double = sunTrueLongitudeDeg - APPARENT_LONGITUDE_ABERRATION - APPARENT_LONGITUDE_NUTATION_AMPLITUDE * sin(toRadians(moonNodeLongitudeDeg)
    )
}
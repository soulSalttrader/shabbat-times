package il.soulSalttrader.shabbattimes.settings.ephemeris

import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.ECCENTRICITY_BASE
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.ECCENTRICITY_PER_CENTURY
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.ECCENTRICITY_PER_CENTURY_SQ
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.MEAN_ANOMALY_BASE
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.MEAN_ANOMALY_PER_CENTURY
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.MEAN_ANOMALY_PER_CENTURY_SQ
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.MEAN_LONGITUDE_BASE
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.MEAN_LONGITUDE_PER_CENTURY
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.MEAN_LONGITUDE_PER_CENTURY_SQ
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.MOON_NODE_LONGITUDE_BASE
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.MOON_NODE_LONGITUDE_PER_CENTURY
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.OBLIQUITY_BASE
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.OBLIQUITY_PER_CENTURY
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.OBLIQUITY_PER_CENTURY_CUBED
import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.OBLIQUITY_PER_CENTURY_SQ
import java.lang.Math.toRadians

object MeanOrbitalElements {
    /**
     * Eccentricity of Earth's orbit.
     * Decreases very slowly over time.
     */
    fun getEccentricity(julianCenturiesSinceJ2000: Double): Double {
        val centuryTerm = ECCENTRICITY_PER_CENTURY + ECCENTRICITY_PER_CENTURY_SQ * julianCenturiesSinceJ2000
        return ECCENTRICITY_BASE - julianCenturiesSinceJ2000 * centuryTerm
    }

    /**
     * Mean anomaly of the Sun (M).
     * Must be normalized to the range [0, 360) degrees before converting to radians.
     */
    fun getMeanAnomalyRad(julianCenturiesSinceJ2000: Double): Double {
        val linearTerm = julianCenturiesSinceJ2000 * MEAN_ANOMALY_PER_CENTURY
        val quadraticTerm = julianCenturiesSinceJ2000 * julianCenturiesSinceJ2000 * MEAN_ANOMALY_PER_CENTURY_SQ
        val meanAnomalyDeg = MEAN_ANOMALY_BASE + linearTerm - quadraticTerm

        var normalized = meanAnomalyDeg % 360.0
        if (normalized < 0.0) normalized += 360.0

        return toRadians(normalized)
    }

    /**
     * Mean longitude of the Sun (L0).
     * Normalized to the range [0, 360) degrees.
     */
    fun getMeanLongitude(julianCenturiesSinceJ2000: Double): Double {
        val linearTerm = julianCenturiesSinceJ2000 * MEAN_LONGITUDE_PER_CENTURY
        val quadraticTerm = julianCenturiesSinceJ2000 * julianCenturiesSinceJ2000 * MEAN_LONGITUDE_PER_CENTURY_SQ
        val meanLongitudeDeg = MEAN_LONGITUDE_BASE + linearTerm + quadraticTerm

        var normalized = meanLongitudeDeg % 360.0
        if (normalized < 0.0) normalized += 360.0

        return normalized
    }

    /**
     * Mean obliquity of the ecliptic (Earth's axial tilt).
     * Decreases very slowly over time.
     */
    fun getMeanObliquityDeg(julianCenturiesSinceJ2000: Double): Double {
        val cubicTerm = OBLIQUITY_PER_CENTURY_SQ - julianCenturiesSinceJ2000 * OBLIQUITY_PER_CENTURY_CUBED
        val quadraticTerm = OBLIQUITY_PER_CENTURY + julianCenturiesSinceJ2000 * cubicTerm
        return OBLIQUITY_BASE - julianCenturiesSinceJ2000 * quadraticTerm
    }

    /**
     * Longitude of the ascending node of the Moon's orbit.
     * Used for nutation calculations.
     */
    fun getMoonNodeLongitudeDeg(
        julianCenturiesSinceJ2000: Double,
    ): Double = MOON_NODE_LONGITUDE_BASE - MOON_NODE_LONGITUDE_PER_CENTURY * julianCenturiesSinceJ2000
}

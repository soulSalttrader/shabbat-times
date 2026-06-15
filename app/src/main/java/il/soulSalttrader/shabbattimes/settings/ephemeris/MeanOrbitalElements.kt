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
    fun getEccentricity(
        julianCenturiesSinceJ2000: Double
    ): Double = ECCENTRICITY_BASE - julianCenturiesSinceJ2000 * (ECCENTRICITY_PER_CENTURY + ECCENTRICITY_PER_CENTURY_SQ * julianCenturiesSinceJ2000)

    /**
     * Mean anomaly of the Sun.
     */
    fun getMeanAnomalyRad(
        julianCenturiesSinceJ2000: Double
    ): Double {
        val meanAnomalyDeg = MEAN_ANOMALY_BASE + julianCenturiesSinceJ2000 * (MEAN_ANOMALY_PER_CENTURY - MEAN_ANOMALY_PER_CENTURY_SQ * julianCenturiesSinceJ2000)
        return toRadians(meanAnomalyDeg)
    }

    /**
     * Mean longitude of the Sun.
     */
    fun getMeanLongitude(
        julianCenturiesSinceJ2000: Double
    ): Double = (MEAN_LONGITUDE_BASE + julianCenturiesSinceJ2000 * (MEAN_LONGITUDE_PER_CENTURY + julianCenturiesSinceJ2000 * MEAN_LONGITUDE_PER_CENTURY_SQ)) % 360.0

    /**
     * Mean obliquity of the ecliptic (Earth's axial tilt).
     * Decreases very slowly over time.
     */
    fun getMeanObliquityDeg(
        julianCenturiesSinceJ2000: Double
    ): Double = OBLIQUITY_BASE - julianCenturiesSinceJ2000 * (OBLIQUITY_PER_CENTURY + julianCenturiesSinceJ2000
            * (OBLIQUITY_PER_CENTURY_SQ - julianCenturiesSinceJ2000 * OBLIQUITY_PER_CENTURY_CUBED))

    /**
     * Longitude of the ascending node of the Moon's orbit.
     * Used for nutation calculations.
     */
    fun getMoonNodeLongitudeDeg(
        julianCenturiesSinceJ2000: Double
    ): Double = MOON_NODE_LONGITUDE_BASE - MOON_NODE_LONGITUDE_PER_CENTURY * julianCenturiesSinceJ2000
}
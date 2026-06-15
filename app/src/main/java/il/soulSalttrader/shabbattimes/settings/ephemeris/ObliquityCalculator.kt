package il.soulSalttrader.shabbattimes.settings.ephemeris

import il.soulSalttrader.shabbattimes.common.constants.SolarConstants.OBLIQUITY_NUTATION_AMPLITUDE
import java.lang.Math.toRadians
import kotlin.math.cos

object ObliquityCalculator {
    /**
     * Corrects mean obliquity with nutation term.
     */
    fun getCorrectedObliquityDeg(
        meanObliquityDeg: Double,
        moonNodeLongitudeDeg: Double,
    ): Double = meanObliquityDeg + OBLIQUITY_NUTATION_AMPLITUDE * cos(toRadians(moonNodeLongitudeDeg))
}
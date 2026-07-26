package il.soulSalttrader.shabbattimes.common.constants

/**
 * Solar ephemeris calculation constants (based on Jean Meeus' Astronomical Algorithms).
 * All angles in degrees unless noted.
 */
object SolarConstants {
    // Mean orbital elements
    const val MEAN_LONGITUDE_BASE = 280.46646
    const val MEAN_LONGITUDE_PER_CENTURY = 36000.76983
    const val MEAN_LONGITUDE_PER_CENTURY_SQ = 0.0003032

    const val MEAN_ANOMALY_BASE = 357.52911
    const val MEAN_ANOMALY_PER_CENTURY = 35999.05029
    const val MEAN_ANOMALY_PER_CENTURY_SQ = 0.0001537

    const val ECCENTRICITY_BASE = 0.016708634
    const val ECCENTRICITY_PER_CENTURY = 0.000042037
    const val ECCENTRICITY_PER_CENTURY_SQ = 0.0000001267

    // Equation of Center coefficients
    const val CENTER_CORRECTION_C1_BASE = 1.914602
    const val CENTER_CORRECTION_C1_PER_CENTURY = 0.004817
    const val CENTER_CORRECTION_C1_PER_CENTURY_SQ = 0.000014
    const val CENTER_CORRECTION_C2_BASE = 0.019993
    const val CENTER_CORRECTION_C2_PER_CENTURY = 0.000101
    const val CENTER_CORRECTION_C3 = 0.000289

    // Obliquity & Nutation
    const val OBLIQUITY_BASE = 23.439291
    const val OBLIQUITY_PER_CENTURY = 0.0130042
    const val OBLIQUITY_PER_CENTURY_SQ = 0.00000016
    const val OBLIQUITY_PER_CENTURY_CUBED = 0.000000504
    const val OBLIQUITY_NUTATION_AMPLITUDE = 0.00256

    const val MOON_NODE_LONGITUDE_BASE = 125.04
    const val MOON_NODE_LONGITUDE_PER_CENTURY = 1934.136

    // Apparent position corrections
    const val APPARENT_LONGITUDE_ABERRATION = 0.00569
    const val APPARENT_LONGITUDE_NUTATION_AMPLITUDE = 0.00478

    const val EQUATION_OF_TIME_MINUTES_PER_DEGREE = 4.0
}
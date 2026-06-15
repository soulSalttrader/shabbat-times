package il.soulSalttrader.shabbattimes.settings.ephemeris

import il.soulSalttrader.shabbattimes.settings.JulianDateConverter
import il.soulSalttrader.shabbattimes.settings.ephemeris.EquationOfTimeCalculator.getDeclinationRad
import il.soulSalttrader.shabbattimes.settings.ephemeris.EquationOfTimeCalculator.getEquationOfTimeMinutes
import il.soulSalttrader.shabbattimes.settings.ephemeris.MeanOrbitalElements.getEccentricity
import il.soulSalttrader.shabbattimes.settings.ephemeris.MeanOrbitalElements.getMeanAnomalyRad
import il.soulSalttrader.shabbattimes.settings.ephemeris.MeanOrbitalElements.getMeanLongitude
import il.soulSalttrader.shabbattimes.settings.ephemeris.MeanOrbitalElements.getMeanObliquityDeg
import il.soulSalttrader.shabbattimes.settings.ephemeris.MeanOrbitalElements.getMoonNodeLongitudeDeg
import il.soulSalttrader.shabbattimes.settings.ephemeris.ObliquityCalculator.getCorrectedObliquityDeg
import il.soulSalttrader.shabbattimes.settings.ephemeris.SunPositionCalculator.getEquationOfCenterDeg
import il.soulSalttrader.shabbattimes.settings.ephemeris.SunPositionCalculator.getSunApparentLongitudeDeg
import il.soulSalttrader.shabbattimes.settings.ephemeris.SunPositionCalculator.getSunTrueLongitudeDeg
import java.time.Instant

class NoaaSolarEphemerisCalculator : SolarEphemerisCalculator {
    /**
     * Computes the solar ephemeris for the given instant.
     *
     * The returned ephemeris contains:
     * - Solar declination (the Sun's angular position north/south of the celestial equator)
     * - Equation of time (the correction between apparent and mean solar time)
     *
     * These values are used to calculate the Sun's apparent position in the sky for a specific observer location.
     */
    override fun ephemerisFor(instant: Instant): SolarEphemeris {
        val julianCenturiesSinceJ2000 = JulianDateConverter.centuriesSinceJ2000(instant)

        // Mean orbital elements
        val meanLongitudeDeg = getMeanLongitude(julianCenturiesSinceJ2000)
        val meanAnomalyRad = getMeanAnomalyRad(julianCenturiesSinceJ2000)
        val eccentricity = getEccentricity(julianCenturiesSinceJ2000)
        val meanObliquityDeg = getMeanObliquityDeg(julianCenturiesSinceJ2000)
        val moonNodeLongitudeDeg = getMoonNodeLongitudeDeg(julianCenturiesSinceJ2000)

        // Sun position calculations
        val equationOfCenterDeg = getEquationOfCenterDeg(meanAnomalyRad, julianCenturiesSinceJ2000)
        val sunTrueLongitudeDeg = getSunTrueLongitudeDeg(meanLongitudeDeg, equationOfCenterDeg)
        val sunApparentLongitudeDeg = getSunApparentLongitudeDeg(sunTrueLongitudeDeg, moonNodeLongitudeDeg)

        // Obliquity
        val correctedObliquityDeg = getCorrectedObliquityDeg(meanObliquityDeg, moonNodeLongitudeDeg)

        // Declination and Equation of Time
        val declinationRad = getDeclinationRad(correctedObliquityDeg, sunApparentLongitudeDeg)
        val equationOfTimeMinutes = getEquationOfTimeMinutes(meanLongitudeDeg, meanAnomalyRad, eccentricity, correctedObliquityDeg)

        return SolarEphemeris(declinationRad, equationOfTimeMinutes)
    }
}
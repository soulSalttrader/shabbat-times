package il.soulSalttrader.shabbattimes.settings.ephemeris

import java.time.Instant

interface SolarEphemerisCalculator {
    fun ephemerisFor(instant: Instant): SolarEphemeris
}

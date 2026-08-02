package il.soulSalttrader.shabbattimes.settings

import java.time.Instant

object JulianDateConverter {
    private const val UNIX_EPOCH_JULIAN_DAY = 2440587.5
    private const val J2000_EPOCH_JULIAN_DAY = 2451545.0
    private const val JULIAN_DAYS_PER_CENTURY = 36525.0
    private const val SECONDS_PER_DAY = 86_400.0

    fun centuriesSinceJ2000(instant: Instant): Double {
        val julianDay = instant.epochSecond / SECONDS_PER_DAY + UNIX_EPOCH_JULIAN_DAY
        return (julianDay - J2000_EPOCH_JULIAN_DAY) / JULIAN_DAYS_PER_CENTURY
    }
}

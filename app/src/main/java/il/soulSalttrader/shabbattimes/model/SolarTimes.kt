package il.soulSalttrader.shabbattimes.model

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

data class SolarTimes(
    val date: LocalDate,
    val sunset: Instant?,
    val dusk: Instant?,
    val nauticalTwilightEnd: Instant?,
    val zoneOffset: ZoneOffset,
)

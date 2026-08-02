package il.soulSalttrader.shabbattimes.settings.solarPosition

import il.soulSalttrader.shabbattimes.model.SolarTimes
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

object SolarTimesFixtures {

    fun default(
        date: LocalDate = LocalDate.of(2026, 6, 21),
        sunset: Instant = Instant.parse("2026-06-21T20:15:00Z"),
        dusk: Instant = Instant.parse("2026-06-21T20:45:00Z"),
        nauticalTwilightEnd: Instant = Instant.parse("2026-06-21T21:30:00Z"),
        zoneOffset: ZoneOffset = ZoneOffset.UTC,
    ): SolarTimes = SolarTimes(
        date = date,
        sunset = sunset,
        dusk = dusk,
        nauticalTwilightEnd = nauticalTwilightEnd,
        zoneOffset = zoneOffset,
    )

    val summerSolsticePrague = default(
        date = LocalDate.of(2026, 6, 21),
        sunset = Instant.parse("2026-06-21T19:50:00Z"),
        dusk = Instant.parse("2026-06-21T20:52:00Z"),
        nauticalTwilightEnd = Instant.parse("2026-06-21T21:45:00Z"),
    )

    val winterSolsticePrague = default(
        date = LocalDate.of(2026, 12, 21),
        sunset = Instant.parse("2026-12-21T15:35:00Z"),
        dusk = Instant.parse("2026-12-21T16:10:00Z"),
        nauticalTwilightEnd = Instant.parse("2026-12-21T16:45:00Z"),
    )
}

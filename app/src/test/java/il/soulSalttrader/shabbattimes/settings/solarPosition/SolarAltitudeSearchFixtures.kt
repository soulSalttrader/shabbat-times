package il.soulSalttrader.shabbattimes.settings.solarPosition

import il.soulSalttrader.shabbattimes.settings.SolarTestCase.SolarAltitudeSearchTestCase
import java.time.Instant

object SolarAltitudeSearchFixtures {

    val summerSolsticePragueCivilTwilight = SolarAltitudeSearchTestCase(
        name = "Civil twilight evening (6°) - Summer Solstice Prague",
        from = Instant.parse("2026-06-21T20:00:00Z"),
        targetAltitudeDeg = -6.0,
        latitude = 50.088,
        longitude = 14.420,
        expectedWindow = Instant.parse("2026-06-21T19:55:00Z")..Instant.parse("2026-06-21T20:05:00Z"),
    )

    val winterSolsticePragueCivilTwilight = SolarAltitudeSearchTestCase(
        name = "Civil twilight evening (6°) - Winter Solstice Prague",
        from = Instant.parse("2026-12-21T15:30:00Z"),
        targetAltitudeDeg = -6.0,
        latitude = 50.088,
        longitude = 14.420,
        expectedWindow = Instant.parse("2026-12-21T15:35:00Z")..Instant.parse("2026-12-21T15:45:00Z"),
    )

    val summerSolsticePragueNauticalTwilight = SolarAltitudeSearchTestCase(
        name = "Nautical twilight evening (12°) - Summer Solstice Prague",
        from = Instant.parse("2026-06-21T20:00:00Z"),
        targetAltitudeDeg = -12.0,
        latitude = 50.088,
        longitude = 14.420,
        expectedWindow = Instant.parse("2026-06-21T21:05:00Z")..Instant.parse("2026-06-21T21:15:00Z"),
    )

    val marchEquinoxPragueCivilTwilight = SolarAltitudeSearchTestCase(
        name = "Civil twilight evening (6°) - March Equinox Prague",
        from = Instant.parse("2026-03-20T17:30:00Z"),
        targetAltitudeDeg = -6.0,
        latitude = 50.088,
        longitude = 14.420,
        expectedWindow = Instant.parse("2026-03-20T17:45:00Z")..Instant.parse("2026-03-20T17:55:00Z"),
    )

    val summerSolsticeGdanskNauticalTwilight = SolarAltitudeSearchTestCase(
        name = "Nautical twilight evening (12°) - Summer Solstice Gdansk",
        from = Instant.parse("2026-06-21T19:30:00Z"),
        targetAltitudeDeg = -12.0,
        latitude = 54.352,
        longitude = 18.646,
        expectedWindow = Instant.parse("2026-06-21T22:15:00Z")..Instant.parse("2026-06-21T22:25:00Z"),
    )

    val summerSolsticeRigaNauticalTwilight = SolarAltitudeSearchTestCase(
        name = "Nautical twilight evening (12°) - Summer Solstice Riga (White Nights)",
        from = Instant.parse("2026-06-21T19:30:00Z"),
        targetAltitudeDeg = -12.0,
        latitude = 56.949,
        longitude = 24.105,
        expectedWindow = null,
    )
}

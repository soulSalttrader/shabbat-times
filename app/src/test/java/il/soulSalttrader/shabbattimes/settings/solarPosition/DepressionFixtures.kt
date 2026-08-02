package il.soulSalttrader.shabbattimes.settings.solarPosition

import il.soulSalttrader.shabbattimes.settings.SolarTestCase.DepressionTestCase
import il.soulSalttrader.shabbattimes.settings.solarPosition.SolarTimesFixtures.summerSolsticePrague
import il.soulSalttrader.shabbattimes.settings.solarPosition.SolarTimesFixtures.winterSolsticePrague
import java.time.Instant

object DepressionFixtures {
    val summerSolsticeCivilTwilightPrague = DepressionTestCase(
        name = "Summer Solstice - Civil Twilight Evening",
        solarData = summerSolsticePrague,
        targetDegrees = 6.0,
        latitude = 50.088,
        longitude = 14.420,
        expectedResult = Instant.parse("2026-06-21T19:58:00Z")..Instant.parse("2026-06-21T20:02:00Z"),
    )

    val winterSolsticeCivilTwilight = DepressionTestCase(
        name = "Winter Solstice - Civil Twilight Evening",
        solarData = winterSolsticePrague,
        targetDegrees = 6.0,
        latitude = 50.088,
        longitude = 14.420,
        expectedResult = Instant.parse("2026-12-21T15:39:00Z")..Instant.parse("2026-12-21T15:42:00Z"),
    )

    val summerSolsticeMorningCivilTwilightPrague = DepressionTestCase(
        name = "Summer Solstice - Morning Civil Twilight",
        solarData = summerSolsticePrague.copy(sunset = Instant.parse("2026-06-21T00:00:00Z")),
        targetDegrees = 6.0,
        latitude = 50.088,
        longitude = 14.420,
        expectedResult = Instant.parse("2026-06-21T02:06:00Z")..Instant.parse("2026-06-21T02:08:00Z"),
    )

    val winterSolsticeMorningCivilTwilightPrague = DepressionTestCase(
        name = "Winter Solstice - Morning Civil Twilight",
        solarData = winterSolsticePrague.copy(sunset = Instant.parse("2026-12-21T04:00:00Z")),
        targetDegrees = 6.0,
        latitude = 50.088,
        longitude = 14.420,
        expectedResult = Instant.parse("2026-12-21T06:19:00Z")..Instant.parse("2026-12-21T06:21:00Z"),
    )

    val summerSolsticeNauticalTwilightPrague = DepressionTestCase(
        name = "SOLAR-DEPRESSION_S1_5 - Evening Nautical Twilight (12°) - Summer Solstice Prague",
        solarData = summerSolsticePrague,
        targetDegrees = 12.0,
        latitude = 50.088,
        longitude = 14.420,
        expectedResult = Instant.parse("2026-06-21T21:06:30Z")..Instant.parse("2026-06-21T21:07:30Z"),
    )

    val winterSolsticeNauticalTwilightPrague = DepressionTestCase(
        name = "SOLAR-DEPRESSION_S1_6 - Evening Nautical Twilight (12°) - Winter Solstice Prague",
        solarData = winterSolsticePrague,
        targetDegrees = 12.0,
        latitude = 50.088,
        longitude = 14.420,
        expectedResult = Instant.parse("2026-12-21T16:21:40Z")..Instant.parse("2026-12-21T16:22:40Z"),
    )

    val summerSolsticeMorningNauticalTwilightPrague = DepressionTestCase(
        name = "SOLAR-DEPRESSION_S1_7 - Morning Nautical Twilight (12°) - Summer Solstice Prague",
        solarData = summerSolsticePrague.copy(sunset = Instant.parse("2026-06-21T00:00:00Z")),
        targetDegrees = 12.0,
        latitude = 50.088,
        longitude = 14.420,
        expectedResult = Instant.parse("2026-06-21T01:01:00Z")..Instant.parse("2026-06-21T01:02:00Z"),
    )

    val winterSolsticeMorningNauticalTwilightPrague = DepressionTestCase(
        name = "SOLAR-DEPRESSION_S1_8 - Morning Nautical Twilight (12°) - Winter Solstice Prague",
        solarData = winterSolsticePrague.copy(sunset = Instant.parse("2026-12-21T04:00:00Z")),
        targetDegrees = 12.0,
        latitude = 50.088,
        longitude = 14.420,
        expectedResult = Instant.parse("2026-12-21T05:37:40Z")..Instant.parse("2026-12-21T05:38:40Z"),
    )
}

package il.soulSalttrader.shabbattimes.settings.solarPosition

import il.soulSalttrader.shabbattimes.settings.SolarTestCase
import java.time.Instant

object ObservationFixtures {
    // Solar zenith
    val summerSolsticeTropicCancerZenith = SolarTestCase.Observation(
        name = "EPHEMERIS-ALTITUDE-S1_1 - should be near zenith (89.99°) at Tropic of Cancer on Summer Solstice 2026",
        instant = Instant.parse("2026-06-21T12:02:00Z"),
        latitude = 23.4394,
        longitude = 0.0,
        expectedAltitudeRange = 89.9..90.0
    )

    val winterSolsticeTropicCapricornZenith = SolarTestCase.Observation(
        name = "EPHEMERIS-ALTITUDE-S1_2 - should be near zenith (89.99°) at Tropic of Capricorn on Winter Solstice 2026",
        instant = Instant.parse("2026-12-21T11:58:00Z"),
        latitude = -23.4394,
        longitude = 0.0,
        expectedAltitudeRange = 89.9..90.0
    )

    // Polar cases
    val northPoleMidnightSun = SolarTestCase.Observation(
        name = "EPHEMERIS-ALTITUDE-S1_3 - should show Midnight Sun (~ +23.44°) at North Pole on Summer Solstice 2026",
        instant = Instant.parse("2026-06-21T00:00:00Z"),
        latitude = 90.0,
        longitude = 0.0,
        expectedAltitudeRange = 23.3..23.6
    )

    val southPolePolarNight = SolarTestCase.Observation(
        name = "EPHEMERIS-ALTITUDE-S1_4 - should show Polar night (~ -23.44°) at South Pole on Winter Solstice 2026",
        instant = Instant.parse("2026-06-21T12:00:00Z"),
        latitude = -90.0,
        longitude = 0.0,
        expectedAltitudeRange = -23.5..-23.3
    )

    // Equation of Time related
    val greenwichEquationOfTimePeak = SolarTestCase.Observation(
        name = "EPHEMERIS-ALTITUDE-S1_5 - should have correct altitude (~ 23.38) at Greenwich during Equation of Time peak (Autumn 2026)",
        instant = Instant.parse("2026-11-03T11:43:30Z"),
        latitude = 51.4778,
        longitude = 0.0,
        expectedAltitudeRange = 23.0..23.8
    )

    // Equinox edge case
    val internationalDateLineSunset = SolarTestCase.Observation(
        name = "EPHEMERIS-ALTITUDE-S1_6 - should be near horizon (~ 1.8°) at International Date Line on March Equinox",
        instant = Instant.parse("2026-03-20T06:00:00Z"),
        latitude = 0.0,
        longitude = 180.0,
        expectedAltitudeRange = 1.6..2.0
    )

    // Location-specific
    val pragueSummerSolsticeSolarNoon = SolarTestCase.Observation(
        name = "EPHEMERIS-ALTITUDE-S1_7 - should have correct noon altitude (~ 61.35° ) in Prague on Summer Solstice 2026",
        instant = Instant.parse("2026-06-21T11:04:00Z"),
        latitude = 50.088,
        longitude = 14.420,
        expectedAltitudeRange = 63.0..63.7
    )
}
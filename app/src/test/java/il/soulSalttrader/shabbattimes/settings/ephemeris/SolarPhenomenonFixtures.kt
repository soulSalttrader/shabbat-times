package il.soulSalttrader.shabbattimes.settings.ephemeris

import java.time.Instant

/**
 * Perihelion and aphelion tests use ±0.2 tolerance because:
 *
 * - These events are astronomically defined by the **minimum/maximum Earth-Sun distance**, not by declination.
 * - They occur a few days after the solstices, when declination is changing relatively slowly, but small errors in the mean anomaly / eccentricity calculation accumulate.
 * - For this low-precision (Meeus/NOAA-style) algorithm, declination accuracy is typically 0.05°–0.25° near these points.
 *
 * In contrast, equinoxes and solstices are tested with tighter tolerances (±0.05°)
 * because they are the primary extrema/zeros of the declination curve.
 */
object SolarPhenomenonFixtures {

    val perihelion = SolarPhenomenon(
        "Perihelion",
        Instant.parse("2026-01-03T17:15:00Z"),
        expectedDeclinationDeg = -22.84,
        expectedEquationOfTimeMinutes = -4.5,
    )

    val marchEquinox = SolarPhenomenon(
        name = "March equinox",
        Instant.parse("2026-03-20T14:46:00Z"),
        expectedDeclinationDeg = 0.0,
        expectedEquationOfTimeMinutes = -7.3,
    )

    val juneSolstice = SolarPhenomenon(
        "June solstice",
        Instant.parse("2026-06-21T08:24:00Z"),
        expectedDeclinationDeg = 23.44,
        expectedEquationOfTimeMinutes = -1.7,
    )

    val aphelion = SolarPhenomenon(
        name = "Aphelion",
        Instant.parse("2026-07-06T17:30:00Z"),
        expectedDeclinationDeg = 22.83,
        expectedEquationOfTimeMinutes = -4.5,
    )

    val septemberEquinox = SolarPhenomenon(
        name = "September equinox",
        Instant.parse("2026-09-23T00:05:00Z"),
        expectedDeclinationDeg = 0.0,
        expectedEquationOfTimeMinutes = 7.3,
    )

    val decemberSolstice = SolarPhenomenon(
        "December solstice",
        Instant.parse("2026-12-21T20:50:00Z"),
        expectedDeclinationDeg = -23.44,
        expectedEquationOfTimeMinutes = 1.7,
    )
}
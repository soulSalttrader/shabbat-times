package il.soulSalttrader.shabbattimes.settings.ephemeris

import il.soulSalttrader.shabbattimes.settings.ephemeris.EphemerisFixtures.aphelion
import il.soulSalttrader.shabbattimes.settings.ephemeris.EphemerisFixtures.decemberSolstice
import il.soulSalttrader.shabbattimes.settings.ephemeris.EphemerisFixtures.juneSolstice
import il.soulSalttrader.shabbattimes.settings.ephemeris.EphemerisFixtures.marchEquinox
import il.soulSalttrader.shabbattimes.settings.ephemeris.EphemerisFixtures.perihelion
import il.soulSalttrader.shabbattimes.settings.ephemeris.EphemerisFixtures.septemberEquinox
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import java.lang.Math.toDegrees

class NoaaSolarEphemerisCalculatorTest : DescribeSpec({
    val calculator: SolarEphemerisCalculator = NoaaSolarEphemerisCalculator()

    describe("EPHEMERIS-NOAA-S1 - Solar declination") {
        withData(
            mapOf(
                "EPHEMERIS-NOAA-S1_1 - should returns declination near -22.84° on the perihelion" to perihelion,
                "EPHEMERIS-NOAA-S1_2 - should returns declination near 0° at March equinox" to marchEquinox,
                "EPHEMERIS-NOAA-S1_3 - should returns declination near +23.44° at June solstice" to juneSolstice,
                "EPHEMERIS-NOAA-S1_4 - should returns declination near 22.83° on the aphelion" to aphelion,
                "EPHEMERIS-NOAA-S1_5 - should returns declination near 0° at September equinox" to septemberEquinox,
                "EPHEMERIS-NOAA-S1_6 - should returns declination near -23.44° at December solstice" to decemberSolstice,
            ),
        ) { testCase ->
            val ephemeris = calculator.ephemerisFor(testCase.instant)
            toDegrees(ephemeris.declinationRad) shouldBe (testCase.expectedDeclinationDeg plusOrMinus testCase.declinationTolerance)
        }
    }

    describe("EPHEMERIS-NOAA-S2 - Equation of time") {
        withData(
            mapOf(
                "EPHEMERIS-NOAA-S2_1 - equation of time should be near -4.5 min on the Perihelion" to perihelion,
                "EPHEMERIS-NOAA-S2_2 - equation of time should be near -7.3 min on March equinox" to marchEquinox,
                "EPHEMERIS-NOAA-S2_3 - equation of time should be near -1.7 min on June solstice" to juneSolstice,
                // The real Sun will pass the meridian 4.50 minutes later
                "EPHEMERIS-NOAA-S2_4 - equation of time should be near -4.5 min on the aphelion" to aphelion,
                "EPHEMERIS-NOAA-S2_5 - equation of time should be near +7.2 min on the September equinox" to septemberEquinox,
                // At solar noon according to the clock, the real Sun has already passed the meridian 1.7 minutes earlier.
                "EPHEMERIS-NOAA-S2_6 - equation of time should be near +1.7 min on December solstice" to decemberSolstice,
            ),
        ) { testCase ->
            val ephemeris = calculator.ephemerisFor(testCase.instant)
            ephemeris.equationOfTimeMinutes shouldBe (testCase.expectedEquationOfTimeMinutes plusOrMinus testCase.equationOfTimeTolerance)
        }
    }
})

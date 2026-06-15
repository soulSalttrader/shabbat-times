package il.soulSalttrader.shabbattimes.settings.ephemeris

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import java.lang.Math.toDegrees


class NoaaSolarEphemerisCalculatorTest : DescribeSpec({
    val calculator: SolarEphemerisCalculator = NoaaSolarEphemerisCalculator()

    describe("EPHEMERIS-NOAA-S1 - Solar declination") {
        it("EPHEMERIS-NOAA-S1_1 - should returns declination near -22.84° on the perihelion") {
            val solarPhenomenon = SolarPhenomenonFixtures.perihelion
            val ephemeris = calculator.ephemerisFor(solarPhenomenon.instant)

            toDegrees(ephemeris.declinationRad) shouldBe (solarPhenomenon.expectedDeclinationDeg plusOrMinus 0.1)
        }

        it("EPHEMERIS-NOAA-S1_2 - should returns declination near 0° at March equinox") {
            val solarPhenomenon = SolarPhenomenonFixtures.marchEquinox
            val ephemeris = calculator.ephemerisFor(solarPhenomenon.instant)

            toDegrees(ephemeris.declinationRad) shouldBe (solarPhenomenon.expectedDeclinationDeg plusOrMinus 0.05)
        }

        it("EPHEMERIS-NOAA-S1_3 - should returns declination near +23.44° at June solstice") {
            val solarPhenomenon = SolarPhenomenonFixtures.juneSolstice
            val ephemeris = calculator.ephemerisFor(solarPhenomenon.instant)

            toDegrees(ephemeris.declinationRad) shouldBe (solarPhenomenon.expectedDeclinationDeg plusOrMinus 0.05)
        }

        it("EPHEMERIS-NOAA-S1_4 - should returns declination near 22.83° on the aphelion") {
            val solarPhenomenon = SolarPhenomenonFixtures.aphelion
            val ephemeris = calculator.ephemerisFor(solarPhenomenon.instant)

            toDegrees(ephemeris.declinationRad) shouldBe (solarPhenomenon.expectedDeclinationDeg plusOrMinus 0.2)
        }

        it("EPHEMERIS-NOAA-S1_5 - should returns declination near 0° at September equinox") {
            val solarPhenomenon = SolarPhenomenonFixtures.septemberEquinox
            val ephemeris = calculator.ephemerisFor(solarPhenomenon.instant)

            toDegrees(ephemeris.declinationRad) shouldBe (solarPhenomenon.expectedDeclinationDeg plusOrMinus 0.05)
        }

        it("EPHEMERIS-NOAA-S1_6 - should returns declination near -23.44° at December solstice") {
            val solarPhenomenon = SolarPhenomenonFixtures.decemberSolstice
            val ephemeris = calculator.ephemerisFor(solarPhenomenon.instant)

            toDegrees(ephemeris.declinationRad) shouldBe (solarPhenomenon.expectedDeclinationDeg plusOrMinus 0.05)
        }
    }

    describe("EPHEMERIS-NOAA-S2 - Equation of time") {
        it("EPHEMERIS-NOAA-S2_1 - equation of time should be near -4.5 min on the Perihelion") {
            val solarPhenomenon = SolarPhenomenonFixtures.perihelion
            val ephemeris = calculator.ephemerisFor(solarPhenomenon.instant)

            ephemeris.equationOfTimeMinutes shouldBe (solarPhenomenon.expectedEquationOfTimeMinutes plusOrMinus 0.1)
        }

        it("EPHEMERIS-NOAA-S2_2 - equation of time should be near -7.3 min on March equinox") {
            val solarPhenomenon = SolarPhenomenonFixtures.marchEquinox
            val ephemeris = calculator.ephemerisFor(solarPhenomenon.instant)

            ephemeris.equationOfTimeMinutes shouldBe (solarPhenomenon.expectedEquationOfTimeMinutes plusOrMinus 0.3)
        }

        it("EPHEMERIS-NOAA-S2_3 - equation of time should be near -1.7 min on June solstice") {
            val solarPhenomenon = SolarPhenomenonFixtures.juneSolstice
            val ephemeris = calculator.ephemerisFor(solarPhenomenon.instant)

            ephemeris.equationOfTimeMinutes shouldBe (solarPhenomenon.expectedEquationOfTimeMinutes plusOrMinus 0.3)
        }

        // The real Sun will pass the meridian 4.50 minutes later
        it("EPHEMERIS-NOAA-S2_4 - equation of time should be near -4.5 min on the aphelion") {
            val solarPhenomenon = SolarPhenomenonFixtures.aphelion
            val ephemeris = calculator.ephemerisFor(solarPhenomenon.instant)

            ephemeris.equationOfTimeMinutes shouldBe (solarPhenomenon.expectedEquationOfTimeMinutes plusOrMinus 0.4)
        }

        it("EPHEMERIS-NOAA-S2_5 - equation of time should be near +7.2 min on the September equinox") {
            val solarPhenomenon = SolarPhenomenonFixtures.septemberEquinox
            val ephemeris = calculator.ephemerisFor(solarPhenomenon.instant)

            ephemeris.equationOfTimeMinutes shouldBe (solarPhenomenon.expectedEquationOfTimeMinutes plusOrMinus 0.3)
        }

        // At solar noon according to the clock, the real Sun has already passed the meridian 1.7 minutes earlier.
        it("EPHEMERIS-NOAA-S2_6 - equation of time should be near +1.7 min on December solstice") {
            val solarPhenomenon = SolarPhenomenonFixtures.decemberSolstice
            val ephemeris = calculator.ephemerisFor(solarPhenomenon.instant)

            ephemeris.equationOfTimeMinutes shouldBe (solarPhenomenon.expectedEquationOfTimeMinutes plusOrMinus 0.3)
        }
    }
})
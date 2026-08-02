package il.soulSalttrader.shabbattimes.settings.solarPosition

import il.soulSalttrader.shabbattimes.settings.ephemeris.NoaaSolarEphemerisCalculator
import il.soulSalttrader.shabbattimes.settings.ephemeris.SolarEphemerisCalculator
import il.soulSalttrader.shabbattimes.settings.solarPosition.ObservationFixtures.greenwichEquationOfTimePeak
import il.soulSalttrader.shabbattimes.settings.solarPosition.ObservationFixtures.internationalDateLineSunset
import il.soulSalttrader.shabbattimes.settings.solarPosition.ObservationFixtures.northPoleMidnightSun
import il.soulSalttrader.shabbattimes.settings.solarPosition.ObservationFixtures.pragueSummerSolsticeSolarNoon
import il.soulSalttrader.shabbattimes.settings.solarPosition.ObservationFixtures.southPolePolarNight
import il.soulSalttrader.shabbattimes.settings.solarPosition.ObservationFixtures.summerSolsticeTropicCancerZenith
import il.soulSalttrader.shabbattimes.settings.solarPosition.ObservationFixtures.winterSolsticeTropicCapricornZenith
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.ranges.shouldBeIn

class EphemerisBasedSolarPositionCalculatorTest : DescribeSpec({
    lateinit var ephemerisCalculator: SolarEphemerisCalculator
    lateinit var calculator: SolarPositionCalculator

    beforeTest {
        ephemerisCalculator = NoaaSolarEphemerisCalculator()
        calculator = EphemerisBasedSolarPositionCalculator(ephemerisCalculator)
    }

    describe("EPHEMERIS-ALTITUDE-S1 - Solar Altitude Calculations") {
        withData(
            nameFn = { it.name },
            summerSolsticeTropicCancerZenith,
            winterSolsticeTropicCapricornZenith,
            northPoleMidnightSun,
            southPolePolarNight,
            greenwichEquationOfTimePeak,
            internationalDateLineSunset,
            pragueSummerSolsticeSolarNoon,
        ) { testCase ->

            val altitude = calculator.altitudeDegrees(
                instant = testCase.instant,
                latitudeDeg = testCase.latitude,
                longitudeDeg = testCase.longitude,
            )

            altitude shouldBeIn testCase.expectedAltitudeRange
        }
    }
})

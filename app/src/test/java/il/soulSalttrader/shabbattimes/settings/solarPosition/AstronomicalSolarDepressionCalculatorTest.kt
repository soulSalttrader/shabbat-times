package il.soulSalttrader.shabbattimes.settings.solarPosition

import il.soulSalttrader.shabbattimes.settings.ephemeris.NoaaSolarEphemerisCalculator
import il.soulSalttrader.shabbattimes.settings.solarPosition.DepressionFixtures.summerSolsticeCivilTwilightPrague
import il.soulSalttrader.shabbattimes.settings.solarPosition.DepressionFixtures.summerSolsticeMorningCivilTwilightPrague
import il.soulSalttrader.shabbattimes.settings.solarPosition.DepressionFixtures.summerSolsticeMorningNauticalTwilightPrague
import il.soulSalttrader.shabbattimes.settings.solarPosition.DepressionFixtures.summerSolsticeNauticalTwilightPrague
import il.soulSalttrader.shabbattimes.settings.solarPosition.DepressionFixtures.winterSolsticeCivilTwilight
import il.soulSalttrader.shabbattimes.settings.solarPosition.DepressionFixtures.winterSolsticeMorningCivilTwilightPrague
import il.soulSalttrader.shabbattimes.settings.solarPosition.DepressionFixtures.winterSolsticeMorningNauticalTwilightPrague
import il.soulSalttrader.shabbattimes.settings.solarPosition.DepressionFixtures.winterSolsticeNauticalTwilightPrague
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.ranges.shouldBeIn

class AstronomicalSolarDepressionCalculatorTest : DescribeSpec({
    lateinit var solarEphemerisCalculator: NoaaSolarEphemerisCalculator
    lateinit var positionCalculator: SolarPositionCalculator
    lateinit var altitudeSearch: SolarAltitudeSearch
    lateinit var calculator: SolarDepressionTimeCalculator

    beforeTest {
        solarEphemerisCalculator = NoaaSolarEphemerisCalculator()
        positionCalculator = EphemerisBasedSolarPositionCalculator(solarEphemerisCalculator)
        altitudeSearch = BinarySolarAltitudeSearch()
        calculator = AstronomicalSolarDepressionCalculator(positionCalculator, altitudeSearch)
    }

    describe("SOLAR-DEPRESSION_S1 - Evening Time For Depression") {
        withData(
            mapOf(
                "SOLAR-DEPRESSION_S1_1 - Evening Civil Twilight (6°) - Summer Solstice Prague" to summerSolsticeCivilTwilightPrague,
                "SOLAR-DEPRESSION_S1_2 - Evening Civil Twilight (6°) - Winter Solstice Prague" to winterSolsticeCivilTwilight,
                "SOLAR-DEPRESSION_S1_3 - Morning Civil Twilight (6°) - Summer Solstice Prague" to summerSolsticeMorningCivilTwilightPrague,
                "SOLAR-DEPRESSION_S1_4 - Morning Civil Twilight (6°) - Winter Solstice Prague" to winterSolsticeMorningCivilTwilightPrague,
                "SOLAR-DEPRESSION_S1_5 - Evening Nautical Twilight (12°) - Summer Solstice Prague" to summerSolsticeNauticalTwilightPrague,
                "SOLAR-DEPRESSION_S1_6 - Evening Nautical Twilight (12°) - Winter Solstice Prague" to winterSolsticeNauticalTwilightPrague,
                "SOLAR-DEPRESSION_S1_7 - Morning Nautical Twilight (12°) - Summer Solstice Prague" to summerSolsticeMorningNauticalTwilightPrague,
                "SOLAR-DEPRESSION_S1_8 - Morning Nautical Twilight (12°) - Winter Solstice Prague" to winterSolsticeMorningNauticalTwilightPrague,
            )
        ) { testCase ->
            val result = calculator.eveningTimeForDepression(
                targetDegrees = testCase.targetDegrees,
                solarData = testCase.solarData,
                latitude = testCase.latitude,
                longitude = testCase.longitude
            )

            result shouldBeIn testCase.expectedResult
        }
    }
})
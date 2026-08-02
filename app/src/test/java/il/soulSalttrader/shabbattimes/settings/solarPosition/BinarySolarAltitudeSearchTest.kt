package il.soulSalttrader.shabbattimes.settings.solarPosition

import il.soulSalttrader.shabbattimes.settings.ephemeris.NoaaSolarEphemerisCalculator
import il.soulSalttrader.shabbattimes.settings.ephemeris.SolarEphemerisCalculator
import il.soulSalttrader.shabbattimes.settings.solarPosition.SolarAltitudeSearchFixtures.marchEquinoxPragueCivilTwilight
import il.soulSalttrader.shabbattimes.settings.solarPosition.SolarAltitudeSearchFixtures.summerSolsticeGdanskNauticalTwilight
import il.soulSalttrader.shabbattimes.settings.solarPosition.SolarAltitudeSearchFixtures.summerSolsticePragueCivilTwilight
import il.soulSalttrader.shabbattimes.settings.solarPosition.SolarAltitudeSearchFixtures.summerSolsticePragueNauticalTwilight
import il.soulSalttrader.shabbattimes.settings.solarPosition.SolarAltitudeSearchFixtures.summerSolsticeRigaNauticalTwilight
import il.soulSalttrader.shabbattimes.settings.solarPosition.SolarAltitudeSearchFixtures.winterSolsticePragueCivilTwilight
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.ranges.shouldBeIn
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class BinarySolarAltitudeSearchTest : DescribeSpec({
    lateinit var solarEphemerisCalculator: SolarEphemerisCalculator
    lateinit var positionCalculator: SolarPositionCalculator
    lateinit var search: SolarAltitudeSearch

    beforeTest {
        solarEphemerisCalculator = NoaaSolarEphemerisCalculator()
        positionCalculator = EphemerisBasedSolarPositionCalculator(solarEphemerisCalculator)
        search = BinarySolarAltitudeSearch()
    }

    describe("SOLAR-BI-SEARCH_S1 - Find Crossing") {
        withData(
            mapOf(
                "SOLAR-BI-SEARCH_S1_1 - Evening Civil Twilight (6°) - Summer Solstice Prague" to summerSolsticePragueCivilTwilight,
                "SOLAR-BI-SEARCH_S1_2 - Evening Civil Twilight (6°) - Winter Solstice Prague" to winterSolsticePragueCivilTwilight,
                "SOLAR-BI-SEARCH_S1_3 - Evening Nautical Twilight (12°) - Summer Solstice Prague" to summerSolsticePragueNauticalTwilight,
                "SOLAR-BI-SEARCH_S1_4 - Evening Civil Twilight (6°) - March Equinox Prague" to marchEquinoxPragueCivilTwilight,
                "SOLAR-BI-SEARCH_S1_5 - Evening Nautical Twilight (12°) - Summer Solstice Gdansk" to summerSolsticeGdanskNauticalTwilight,
                "SOLAR-BI-SEARCH_S1_6 - Evening Nautical Twilight (12°) - Summer Solstice Riga (White Nights)" to summerSolsticeRigaNauticalTwilight,
            ),
        ) { testCase ->

            val result = search.findCrossing(
                from = testCase.from,
                targetAltitudeDeg = testCase.targetAltitudeDeg,
                positionCalculator = positionCalculator,
                latitudeDeg = testCase.latitude,
                longitudeDeg = testCase.longitude,
            )

            when {
                testCase.expectedWindow != null -> {
                    result shouldNotBe null
                    result!! shouldBeIn testCase.expectedWindow
                }

                else -> {
                    result shouldBe null
                }
            }
        }
    }
})

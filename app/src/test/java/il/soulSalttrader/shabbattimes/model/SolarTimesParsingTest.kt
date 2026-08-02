package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.network.dto.SolarTimesResultDto
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class SolarTimesParsingTest : DescribeSpec({
    describe("PARSER_DATE_S1 - SolarTimesResultDto.toDomain()") {
        it("PARSER_DATE_S1_1 - should handle empty strings by returning null instants") {
            val dto = SolarTimesResultDto(
                date = "2024-05-20",
                sunset = "",
                dusk = "",
                nauticalTwilightEnd = "",
                utcOffsetMinutes = 120,
            )

            val domain = dto.toDomain()

            domain.date shouldBe LocalDate.of(2024, 5, 20)
            domain.sunset shouldBe null
            domain.dusk shouldBe null
            domain.nauticalTwilightEnd shouldBe null
        }

        it("PARSER_DATE_S1_2 - should parse valid times correctly") {
            val dto = SolarTimesResultDto(
                date = "2024-05-20",
                sunset = "19:30:00",
                dusk = "20:00:00",
                nauticalTwilightEnd = "20:30:00",
                utcOffsetMinutes = 120,
            )

            val domain = dto.toDomain()

            domain.sunset shouldBe LocalDate.of(2024, 5, 20).atTime(19, 30).atZone(domain.zoneOffset).toInstant()
            domain.dusk shouldBe LocalDate.of(2024, 5, 20).atTime(20, 0).atZone(domain.zoneOffset).toInstant()
        }
    }
})

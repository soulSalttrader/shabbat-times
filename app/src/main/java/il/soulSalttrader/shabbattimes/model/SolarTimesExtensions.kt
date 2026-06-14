package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.common.constants.DateTimeFormatters.API_DATE_PARSER
import il.soulSalttrader.shabbattimes.common.parseRollingOver
import il.soulSalttrader.shabbattimes.network.dto.SolarTimesResultDto
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

fun SolarTimesResultDto.toDomain(): SolarTimes {
    val localDate = LocalDate.parse(date, API_DATE_PARSER)
    val zoneOffset = ZoneOffset.ofTotalSeconds(utcOffsetMinutes * 60)

    val sunsetInstant = parseRollingOver(
        raw = sunset,
        date = localDate,
        zoneOffset = zoneOffset,
        referenceInstant = Instant.MIN,
    )

    return SolarTimes(
        date = localDate,
        sunset = sunsetInstant,
        dusk = parseRollingOver(dusk, localDate, zoneOffset, sunsetInstant),
        nauticalTwilightEnd = parseRollingOver(nauticalTwilightEnd, localDate, zoneOffset, sunsetInstant),
        zoneOffset = zoneOffset,
    )
}
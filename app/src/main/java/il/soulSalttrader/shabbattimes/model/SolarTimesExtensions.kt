package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.common.constants.DateTimeFormatters.API_DATE_PARSER
import il.soulSalttrader.shabbattimes.common.parseRollingOver
import il.soulSalttrader.shabbattimes.network.dto.SolarTimesResultDto
import il.soulSalttrader.shabbattimes.settings.HavdalahCriterion
import il.soulSalttrader.shabbattimes.settings.ShabbatPreferences
import il.soulSalttrader.shabbattimes.settings.solarPosition.SolarDepressionTimeCalculator
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

fun SolarTimesResultDto.toDomain(): SolarTimes {
    val localDate = LocalDate.parse(date, API_DATE_PARSER)
    val zoneOffset = ZoneOffset.ofTotalSeconds(utcOffsetMinutes * 60)

    val sunsetInstant = parseRollingOver(
        raw = sunset,
        date = localDate,
        zoneOffset = zoneOffset,
        referenceInstant = null,
    )

    return SolarTimes(
        date = localDate,
        sunset = sunsetInstant,
        dusk = parseRollingOver(dusk, localDate, zoneOffset, sunsetInstant),
        nauticalTwilightEnd = parseRollingOver(nauticalTwilightEnd, localDate, zoneOffset, sunsetInstant),
        zoneOffset = zoneOffset,
    )
}

fun SolarTimes.resolveCandleLighting(
    preferences: ShabbatPreferences,
): TimeState {
    val offset = preferences.candleLightingOffset
    val instant = sunset?.minus(offset.minutes, ChronoUnit.MINUTES)
    return instant?.toTimeState(zoneOffset) ?: TimeState.Unavailable(offset)
}

fun SolarTimes.resolveHavdalah(
    preferences: ShabbatPreferences,
    startEvent: SolarTimesRequest,
    havdalahCalculator: SolarDepressionTimeCalculator,
): TimeState {
    val criterion = preferences.havdalahCriterion
    val instant = when (criterion) {
        is HavdalahCriterion.Solar -> havdalahCalculator.eveningTimeForDepression(
            criterion.depression.degrees,
            this,
            startEvent.coordinates.latitude,
            startEvent.coordinates.longitude,
        )
        is HavdalahCriterion.Fixed -> sunset?.plus(criterion.offset.minutes, ChronoUnit.MINUTES)
    }
    return instant?.toTimeState(zoneOffset) ?: TimeState.Unavailable(criterion)
}

private fun Instant.toTimeState(zone: ZoneOffset) = TimeState.Available(
    time = atZone(zone).toLocalTime(),
    date = atZone(zone).toLocalDate(),
)

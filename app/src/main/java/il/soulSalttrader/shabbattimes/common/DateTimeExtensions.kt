package il.soulSalttrader.shabbattimes.common

import il.soulSalttrader.shabbattimes.common.constants.DateTimeFormatters.API_TIME_PARSER_24
import il.soulSalttrader.shabbattimes.common.constants.DateTimeFormatters.HEBREW_DATE_FORMATTER
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDate.now
import java.time.LocalTime
import java.time.ZoneOffset

fun LocalDate.nextOrTodayDayOfWeek(target: DayOfWeek): LocalDate {
    val candidate = this.with(target)

    return when {
        candidate.isBefore(this) -> candidate.plusWeeks(1)
        else                     -> candidate
    }
}

fun upcomingFriday(): LocalDate = now().nextOrTodayDayOfWeek(DayOfWeek.FRIDAY)
fun upcomingSaturday(): LocalDate = now().nextOrTodayDayOfWeek(DayOfWeek.SATURDAY)
fun LocalDate.toDisplayString(): String = this.format(HEBREW_DATE_FORMATTER)

/**
 * Parses [raw] as a time-of-day on [date] in [zoneOffset], rolling over to the next day
 * if the result would fall before [referenceInstant] (handles times that cross midnight,
 * e.g. dusk after 00:00 when sunset is late in the evening).
 */
fun parseRollingOver(
    raw: String,
    date: LocalDate,
    zoneOffset: ZoneOffset,
    referenceInstant: Instant,
): Instant {
    val time = LocalTime.parse(raw, API_TIME_PARSER_24)
    val sameDay = date.atTime(time).toInstant(zoneOffset)

    return when (sameDay.isBefore(referenceInstant))  {
        true -> date.plusDays(1).atTime(time).toInstant(zoneOffset)
        else -> sameDay
    }
}

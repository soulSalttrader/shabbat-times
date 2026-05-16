package il.soulSalttrader.shabbattimes.common

import il.soulSalttrader.shabbattimes.common.constants.DateTimeFormatters.API_DATE_PARSER
import il.soulSalttrader.shabbattimes.common.constants.DateTimeFormatters.API_TIME_PARSER_24
import il.soulSalttrader.shabbattimes.common.constants.DateTimeFormatters.HEBREW_DATE_FORMATTER
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDate.now
import java.time.LocalTime

fun LocalDate.nextOrTodayDayOfWeek(target: DayOfWeek): LocalDate {
    val candidate = this.with(target)

    return when {
        candidate.isBefore(this) -> candidate.plusWeeks(1)
        else -> candidate
    }
}

fun upcomingFriday(): LocalDate = now().nextOrTodayDayOfWeek(DayOfWeek.FRIDAY)
fun upcomingSaturday(): LocalDate = now().nextOrTodayDayOfWeek(DayOfWeek.SATURDAY)

fun String.toLocalDate(): LocalDate = LocalDate.parse(this, API_DATE_PARSER)
fun String.toLocalTime(): LocalTime = LocalTime.parse(this, API_TIME_PARSER_24)
fun LocalDate.toDisplayString(): String = this.format(HEBREW_DATE_FORMATTER)

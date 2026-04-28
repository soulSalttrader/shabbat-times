package il.soulSalttrader.shabbattimes.common

import android.content.Context
import android.text.format.DateFormat
import il.soulSalttrader.shabbattimes.constants.DateTimeFormatters.API_DATE_PARSER
import il.soulSalttrader.shabbattimes.constants.DateTimeFormatters.API_TIME_PARSER_24
import il.soulSalttrader.shabbattimes.constants.DateTimeFormatters.HEBREW_DATE_FORMATTER
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDate.now
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDate.nextOrTodayDayOfWeek(target: DayOfWeek): LocalDate {
    val candidate = this.with(target)

    return when {
        candidate.isBefore(this) -> candidate.plusWeeks(1)
        else -> candidate
    }
}

fun upcomingCandleLightingDate(): LocalDate = now().nextOrTodayDayOfWeek(DayOfWeek.FRIDAY)
fun upcomingHavdalahDate(): LocalDate = now().nextOrTodayDayOfWeek(DayOfWeek.SATURDAY)

fun LocalTime.toDisplayString(context: Context): String {
    val use24 = DateFormat.is24HourFormat(context)
    val pattern = if (use24) "HH:mm" else "h:mm a"
    return this.format(DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))
}

fun String.toLocalDate(): LocalDate = LocalDate.parse(this, API_DATE_PARSER)
fun String.toLocalTime(): LocalTime = LocalTime.parse(this, API_TIME_PARSER_24)
fun LocalDate.toDisplayString(): String = this.format(HEBREW_DATE_FORMATTER)

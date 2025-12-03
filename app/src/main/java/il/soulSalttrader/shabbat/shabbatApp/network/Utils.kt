package il.soulSalttrader.retro.shabbatApp.network

import android.content.Context
import android.text.format.DateFormat
import il.soulSalttrader.retro.shabbatApp.network.DataFormatter.apiDateParser
import il.soulSalttrader.retro.shabbatApp.network.DataFormatter.apiTimeParser12
import il.soulSalttrader.retro.shabbatApp.network.DataFormatter.apiTimeParser24
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDate.now
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DataFormatter {
    val hebrewDateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val apiDateParser: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val apiTimeParser24: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val apiTimeParser12: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")
}

fun LocalDate.nextOrTodayDayOfWeek(target: DayOfWeek): LocalDate {
    val candidate = this.with(target)

    return when {
        candidate.isBefore(this) -> candidate.plusWeeks(1)
        else -> candidate
    }
}

fun LocalDate.toStringWith(formatter: DateTimeFormatter): String = this.format(formatter)

fun upcomingCandleLightingDate(): LocalDate = now().nextOrTodayDayOfWeek(DayOfWeek.FRIDAY)
fun upcomingHavdalahDate(): LocalDate = now().nextOrTodayDayOfWeek(DayOfWeek.SATURDAY)

fun LocalTime.toDisplayString(context: Context): String {
    val use24 = DateFormat.is24HourFormat(context)
    val pattern = if (use24) "HH:mm" else "h:mm a"
    return this.format(DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))
}

fun String.toLocalTimeFromApi(is24hFormat: Boolean): LocalTime =
    if (is24hFormat) LocalTime.parse(this, apiTimeParser24)
    else LocalTime.parse(this, apiTimeParser12)

fun LocalDate.toDisplayString(): String = this.format(apiDateParser)
fun String.toLocalDateFromApi(): LocalDate = LocalDate.parse(this, apiDateParser)
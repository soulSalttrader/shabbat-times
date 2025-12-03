package il.soulSalttrader.retro.shabbatApp.network

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDate.now
import java.time.format.DateTimeFormatter

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
package il.soulSalttrader.retro.shabbatApp.network

import il.soulSalttrader.retro.shabbatApp.network.DataFormatter.usStyle
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDate.now
import java.time.format.DateTimeFormatter

object DataFormatter {
    val usStyle: DateTimeFormatter = DateTimeFormatter.ofPattern("mm/dd/yyyy")
    val bohemianStyle: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/mm/yyyy")
    val hebrewStyle: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val displayDate: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
    val displayDateWithDay: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMM dd")
}

fun LocalDate.nextOrTodayDayOfWeek(target: DayOfWeek): LocalDate {
    val candidate = this.with(target)

    return when {
        candidate.isBefore(this) -> candidate.plusWeeks(1)
        else -> candidate
    }
}

fun LocalDate.toStringWith(formatter: DateTimeFormatter = usStyle): String = this.format(formatter)

fun upcomingCandleLighting(): LocalDate = now().nextOrTodayDayOfWeek(DayOfWeek.FRIDAY)
fun upcomingShabbatHavdalah(): LocalDate = now().nextOrTodayDayOfWeek(DayOfWeek.SATURDAY)
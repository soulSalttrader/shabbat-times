package il.soulSalttrader.retro.shabbatApp.network

import java.time.format.DateTimeFormatter

object DataFormatter {
    val usStyle: DateTimeFormatter = DateTimeFormatter.ofPattern("mm/dd/yyyy")
    val bohemianStyle: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/mm/yyyy")
    val hebrewStyle: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val displayDate: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
    val displayDateWithDay: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMM dd")
}
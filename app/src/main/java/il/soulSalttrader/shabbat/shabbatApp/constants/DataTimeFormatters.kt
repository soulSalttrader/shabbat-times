package il.soulSalttrader.retro.shabbatApp.constants

import java.time.format.DateTimeFormatter

object DataTimeFormatters {
    const val HEBREW_DATE_PATTERN = "dd MMM yyyy"
    val API_DATE_PARSER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE // "yyyy-MM-dd"
    val API_TIME_PARSER_24: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME // "HH:mm:ss.SSS"
    val API_TIME_PARSER_12: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")
}
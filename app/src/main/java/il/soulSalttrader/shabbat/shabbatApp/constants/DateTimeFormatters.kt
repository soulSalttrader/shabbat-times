package il.soulSalttrader.retro.shabbatApp.constants

import java.time.format.DateTimeFormatter

object DateTimeFormatters {
    const val HEBREW_DATE_PATTERN = "dd MMM yyyy"

    val HEBREW_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(HEBREW_DATE_PATTERN)
    val API_DATE_PARSER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE // "yyyy-MM-dd"
    val API_TIME_PARSER_24: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME // "HH:mm:ss.SSS"
    val API_TIME_PARSER_12: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")
}
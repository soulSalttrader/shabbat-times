package il.soulSalttrader.shabbattimes.common.constants

import java.time.format.DateTimeFormatter

object DateTimeFormatters {
    const val API_DATE_PATTERN = "yyyy-MM-dd"
    const val TIME_24H = "HH:mm"
    const val TIME_12H = "h:mm a"

    val API_DATE_PARSER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE // "yyyy-MM-dd"
    val API_TIME_PARSER_24: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME // "HH:mm:ss.SSS"
    val API_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(API_DATE_PATTERN)
}

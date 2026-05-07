package il.soulSalttrader.shabbattimes.model

import java.time.LocalDate
import java.time.ZoneId

data class SolarTimesRequest(
    val date: LocalDate,
    val coordinates: Coordinates,
    val timeZone: ZoneId,
)

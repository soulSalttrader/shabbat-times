package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.location.LocationStatus
import java.time.LocalDate
import java.time.LocalTime

data class HalachicTimes(
    val city: City,
    val candleLightingTime: LocalTime,
    val candleLightingDate: LocalDate,
    val havdalahTime: LocalTime,
    val havdalahDate: LocalDate,
    val locationStatus: LocationStatus,
)
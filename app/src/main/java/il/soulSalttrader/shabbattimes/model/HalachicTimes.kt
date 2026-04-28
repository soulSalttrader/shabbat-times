package il.soulSalttrader.shabbattimes.model

import java.time.LocalDate
import java.time.LocalTime

data class HalachicTimes(
    val coordinates: Coordinates,
    val candleLightingTime: LocalTime,
    val candleLightingDate: LocalDate,
    val havdalahTime: LocalTime,
    val havdalahDate: LocalDate,
)
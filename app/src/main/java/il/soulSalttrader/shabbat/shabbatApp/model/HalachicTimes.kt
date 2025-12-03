package il.soulSalttrader.retro.shabbatApp.model

import java.time.LocalDate
import java.time.LocalTime

data class HalachicTimes(
    val candleLightingTime: LocalTime,
    val candleLightingDate: LocalDate,
    val havdalahTime: LocalTime,
    val havdalahDate: LocalDate,
)
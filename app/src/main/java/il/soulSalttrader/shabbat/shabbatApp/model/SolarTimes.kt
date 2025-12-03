package il.soulSalttrader.retro.shabbatApp.model

import java.time.LocalDate
import java.time.LocalTime

data class SolarTimes(
    val date: LocalDate,
    val sunset: LocalTime,
)
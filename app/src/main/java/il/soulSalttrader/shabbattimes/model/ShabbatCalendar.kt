package il.soulSalttrader.shabbattimes.model

import java.time.LocalDate

interface ShabbatCalendar {
    fun upcomingCandleLightingDate(): LocalDate
    fun upcomingHavdalahDate(): LocalDate
}
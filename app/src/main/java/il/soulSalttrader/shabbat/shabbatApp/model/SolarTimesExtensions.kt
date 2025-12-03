package il.soulSalttrader.retro.shabbatApp.model

import il.soulSalttrader.retro.shabbatApp.common.toLocalDateFromApi
import il.soulSalttrader.retro.shabbatApp.common.toLocalTimeFromApi
import il.soulSalttrader.retro.shabbatApp.network.dto.SolarTimes

fun SolarTimes.toDomain(is24Format: Boolean) = SolarTimes(
    date = this.date.toLocalDateFromApi(),
    sunset = this.sunset.toLocalTimeFromApi(is24Format),
)
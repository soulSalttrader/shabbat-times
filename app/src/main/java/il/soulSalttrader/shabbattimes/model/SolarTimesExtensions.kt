package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.common.toLocalDateFromApi
import il.soulSalttrader.shabbattimes.common.toLocalTimeFromApi
import il.soulSalttrader.shabbattimes.network.dto.SolarTimes

fun SolarTimes.toDomain(is24Format: Boolean) = SolarTimes(
    date = this.date.toLocalDateFromApi(),
    sunset = this.sunset.toLocalTimeFromApi(is24Format),
)
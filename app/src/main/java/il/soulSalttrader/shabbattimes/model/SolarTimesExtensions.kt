package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.common.toLocalDate
import il.soulSalttrader.shabbattimes.common.toLocalTime
import il.soulSalttrader.shabbattimes.network.dto.SolarTimesResultDto

fun SolarTimesResultDto.toDomain() = SolarTimes(
    date = date.toLocalDate(),
    sunset = sunset.toLocalTime(),
)
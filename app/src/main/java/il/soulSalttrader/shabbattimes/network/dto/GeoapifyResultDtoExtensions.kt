package il.soulSalttrader.shabbattimes.network.dto

import il.soulSalttrader.shabbattimes.content.city.CityStatus
import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.model.Coordinates
import java.time.ZoneId
import java.util.UUID

fun GeoapifyResultDto.toCityDomain(): City = City(
    id = placeId ?: UUID.randomUUID().toString(),
    name = cityName ?: placeName ?: "Unknown",
    coordinates = Coordinates(latitude ?: 0.0, longitude ?: 0.0),
    status = CityStatus.Unknown,
    timeZone = ZoneId.of(timezone?.name ?: ZoneId.systemDefault().id),
    timeFormat = 24,
)
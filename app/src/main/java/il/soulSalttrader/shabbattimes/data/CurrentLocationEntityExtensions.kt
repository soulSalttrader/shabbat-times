package il.soulSalttrader.shabbattimes.data

import il.soulSalttrader.shabbattimes.model.Coordinates
import il.soulSalttrader.shabbattimes.model.SavedLocation
import java.time.ZoneId

fun CurrentLocationEntity.toDomain() = SavedLocation(
    id = id,
    name = name,
    coordinates = Coordinates(latitude, longitude),
    timeZoneId = ZoneId.of(timeZoneId),
)

fun SavedLocation.toCurrentLocationEntity() = CurrentLocationEntity(
    rowId = 0, // always 0 — single row
    id = id,
    name = name,
    latitude = coordinates.latitude,
    longitude = coordinates.longitude,
    timeZoneId = timeZoneId.id,
)
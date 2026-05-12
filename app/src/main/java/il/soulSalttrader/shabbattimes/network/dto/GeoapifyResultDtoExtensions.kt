package il.soulSalttrader.shabbattimes.network.dto

import android.util.Log
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.model.Coordinates
import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.model.normalize
import java.time.ZoneId
import java.util.UUID

fun GeoapifyResultDto.toResolvedLocation(
    requestCoordinates: Coordinates? = null
): ResolvedLocation {
    val apiCoordinates = Coordinates(latitude ?: 0.0, longitude ?: 0.0).normalize()
    if (Debug.enabled) { Log.d("ResolvedLocation", "request: $requestCoordinates api: $apiCoordinates") }

    return ResolvedLocation(
        id = placeId ?: UUID.randomUUID().toString(),
        name = cityName ?: placeName ?: "Unknown",
        coordinates = requestCoordinates ?: apiCoordinates,
        timeZoneId = ZoneId.of(timezone?.name ?: ZoneId.systemDefault().id),
    )
}
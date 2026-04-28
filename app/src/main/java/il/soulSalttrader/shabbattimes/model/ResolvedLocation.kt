package il.soulSalttrader.shabbattimes.model

import android.util.Log
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.network.dto.GeoapifyResultDto
import kotlinx.serialization.Serializable
import java.time.ZoneId

@Serializable()
data class ResolvedLocation(
    val name: String,
    val coordinates: Coordinates,
    @Serializable(with = ZoneIdAsStringSerializer::class)
    val timeZoneId: ZoneId,
)

fun GeoapifyResultDto.toResolvedLocation(
    requestCoordinates: Coordinates? = null
): ResolvedLocation {
    val apiCoordinates = Coordinates(latitude ?: 0.0, longitude ?: 0.0).normalize()
    if (Debug.enabled) { Log.d("ResolvedLocation", "request: $requestCoordinates api: $apiCoordinates") }

    return ResolvedLocation(
        name = cityName ?: placeName ?: "Unknown",
        coordinates = requestCoordinates ?: apiCoordinates,
        timeZoneId = ZoneId.of(timezone?.name ?: ZoneId.systemDefault().id),
    )
}
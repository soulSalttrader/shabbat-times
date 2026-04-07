package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.location.LocationStatus
import java.time.ZoneId
import java.util.UUID

class CityBuilder {
    var id: String? = null
    var name: String? = null
    var coordinates: Coordinates? = null
    var locationStatus: LocationStatus = LocationStatus.Unknown
    var timeZone: ZoneId? = null
    var timeFormat: Int? = null

    fun build(): City {
        val safeName = name ?: throw IllegalStateException("Name required")
        val safeCoordinates = coordinates ?: throw IllegalStateException("Coordinates required")

        return City(
            id = id ?: UUID.randomUUID().toString(),
            name = safeName,
            coordinates = safeCoordinates,
            locationStatus = locationStatus,
            timeZone = timeZone ?: ZoneId.systemDefault(),
            timeFormat = timeFormat ?: 24,
        )
    }
}
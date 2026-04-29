package il.soulSalttrader.shabbattimes.model

import kotlinx.serialization.Serializable
import java.time.ZoneId

@Serializable
data class SavedLocation(
    val id: String,
    val name: String,
    val coordinates: Coordinates,
    @Serializable(with = ZoneIdAsStringSerializer::class)
    val timeZoneId: ZoneId,
) {
    companion object {
        const val GPS_ID = "gps"
        const val EMPTY_ID = "empty"

        fun empty() = SavedLocation(
            id = EMPTY_ID,
            name = "",
            coordinates = Coordinates(0.0, 0.0),
            timeZoneId = ZoneId.systemDefault(),
        )

        fun isEmpty(location: SavedLocation) = location.id == EMPTY_ID
    }
}
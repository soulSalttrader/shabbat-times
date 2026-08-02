package il.soulSalttrader.shabbattimes.model

import androidx.compose.runtime.Immutable
import java.time.ZoneId
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class SavedLocation(
    val id: String,
    val name: String,
    val coordinates: Coordinates,
    @Serializable(with = ZoneIdAsStringSerializer::class)
    val timeZoneId: ZoneId,
) {
    companion object {
        const val EMPTY_NAME = "Tap to use current location"
        const val GPS_ID = "gps"
        const val EMPTY_ID = "empty"
        const val LOCATION_ID = "location"

        fun empty() = SavedLocation(
            id = EMPTY_ID,
            name = EMPTY_NAME,
            coordinates = Coordinates.EMPTY,
            timeZoneId = ZoneId.systemDefault(),
        )

        fun isEmpty(location: SavedLocation) = location.id == EMPTY_ID
    }
}

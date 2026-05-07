package il.soulSalttrader.shabbattimes.model

import androidx.compose.runtime.Immutable
import il.soulSalttrader.shabbattimes.location.LocationStatus
import il.soulSalttrader.shabbattimes.location.toLabel
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class ShabbatEntry(
    val location: SavedLocation,
    val times: HalachicTimesDisplay?,
    val status: LocationStatus,
) {
    val label: String = status.toLabel()
}
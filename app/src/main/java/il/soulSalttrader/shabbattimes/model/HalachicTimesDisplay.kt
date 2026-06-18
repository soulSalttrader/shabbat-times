package il.soulSalttrader.shabbattimes.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class HalachicTimesDisplay(
    val coordinates: Coordinates,
    val candleLighting: TimeState,
    val havdalah: TimeState,
) {
    companion object {
        const val EMPTY_TIME = "--:--"
        const val EMPTY_DATE = "dd/mm/yyyy"
    }
}
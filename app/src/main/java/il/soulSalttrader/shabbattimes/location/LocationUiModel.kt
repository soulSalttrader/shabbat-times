package il.soulSalttrader.shabbattimes.location

import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay
import il.soulSalttrader.shabbattimes.model.SavedLocation

data class LocationUiModel(
    val location: SavedLocation,
    val times: HalachicTimesDisplay,
    val status: LocationStatus,
)

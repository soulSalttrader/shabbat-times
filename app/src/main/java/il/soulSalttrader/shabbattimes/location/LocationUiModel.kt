package il.soulSalttrader.shabbattimes.location

import il.soulSalttrader.shabbattimes.model.SavedLocation

data class LocationUiModel(
    val location: SavedLocation,
    val status: LocationStatus,
)

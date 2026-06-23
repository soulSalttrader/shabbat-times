package il.soulSalttrader.shabbattimes.model

//sealed interface CurrentLocationState {
//    data object Idle : CurrentLocationState
//    data object Fetching : CurrentLocationState
//    data class Available(val location: SavedLocation) : CurrentLocationState
//}

sealed interface CurrentLocationState {
    data object Idle : CurrentLocationState
    data object Fetching : CurrentLocationState
    data class Available(
        val coordinates: Coordinates  // ← just coordinates, not full SavedLocation
    ) : CurrentLocationState
}
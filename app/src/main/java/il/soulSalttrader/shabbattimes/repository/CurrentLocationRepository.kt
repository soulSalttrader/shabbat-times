package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.model.SavedLocation
import kotlinx.coroutines.flow.StateFlow

interface CurrentLocationRepository {
    val location: StateFlow<SavedLocation?>
    suspend fun update(location: SavedLocation?)
}
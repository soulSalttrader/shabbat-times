package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.common.constants.LocationConfig.MAX_SAVED_LOCATIONS
import il.soulSalttrader.shabbattimes.model.SavedLocation
import kotlinx.coroutines.flow.StateFlow

interface SavedLocationsRepository {
    val locations: StateFlow<List<SavedLocation>>
    fun isLimitReached(): Boolean = locations.value.size >= MAX_SAVED_LOCATIONS
    suspend fun save(location: SavedLocation)
    suspend fun remove(location: SavedLocation)
    suspend fun reorder(locations: List<SavedLocation>)
}
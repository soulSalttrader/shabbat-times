package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.model.SavedLocation
import kotlinx.coroutines.flow.StateFlow

interface SavedLocationsRepository {
    val locations: StateFlow<List<SavedLocation>>
    suspend fun save(location: SavedLocation)
    suspend fun remove(location: SavedLocation)
}
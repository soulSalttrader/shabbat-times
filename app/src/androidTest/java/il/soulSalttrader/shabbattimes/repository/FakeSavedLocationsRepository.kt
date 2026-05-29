package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.model.SavedLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeSavedLocationsRepository : SavedLocationsRepository {
    private val _locations = MutableStateFlow<List<SavedLocation>>(emptyList())
    override val locations: StateFlow<List<SavedLocation>> = _locations

    override suspend fun save(location: SavedLocation) {
        _locations.value += location
    }

    override suspend fun remove(location: SavedLocation) {
        _locations.value -= location
    }

    override suspend fun reorder(locations: List<SavedLocation>) {
        _locations.value = locations
    }

    fun clear() { _locations.value = emptyList() }
}
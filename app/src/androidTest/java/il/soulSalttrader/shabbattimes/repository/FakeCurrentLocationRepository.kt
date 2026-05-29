package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.model.SavedLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeCurrentLocationRepository : CurrentLocationRepository {
    private val _location = MutableStateFlow<SavedLocation?>(null)
    override val location: StateFlow<SavedLocation?> = _location

    override suspend fun update(location: SavedLocation?) {
        _location.value = location
    }

    fun clear() { _location.value = null }
}
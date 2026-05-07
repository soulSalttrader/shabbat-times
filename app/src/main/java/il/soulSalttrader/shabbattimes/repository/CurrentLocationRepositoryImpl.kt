package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.model.SavedLocation
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Singleton
class CurrentLocationRepositoryImpl @Inject constructor() : CurrentLocationRepository {
    private val _currentLocation = MutableStateFlow<SavedLocation?>(null)
    override val location: StateFlow<SavedLocation?> = _currentLocation

    override suspend fun update(location: SavedLocation?) {
        _currentLocation.value = location
    }
}
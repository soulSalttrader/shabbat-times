package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.common.constants.LocationConfig.MAX_SAVED_LOCATIONS
import il.soulSalttrader.shabbattimes.model.SavedLocation
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@Singleton
class SavedLocationsRepositoryInMemory @Inject constructor() : SavedLocationsRepository {
    private val _locations: MutableStateFlow<List<SavedLocation>> = MutableStateFlow(emptyList())
    override val locations: StateFlow<List<SavedLocation>> = _locations

    override suspend fun save(location: SavedLocation) {
        _locations.update { current ->
            when {
                current.size >= MAX_SAVED_LOCATIONS -> current
                current.any { it.id == location.id } -> current.map { if (it.id == location.id) location else it }
                else -> current + location
            }
        }
    }

    override suspend fun remove(location: SavedLocation) {
        _locations.update { it.filter { loc -> loc.id != location.id } }
    }

    override suspend fun reorder(locations: List<SavedLocation>) {
        _locations.update { locations }
    }
}
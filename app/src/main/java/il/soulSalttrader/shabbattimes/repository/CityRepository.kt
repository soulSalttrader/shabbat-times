package il.soulSalttrader.shabbattimes.repository

import android.location.Location
import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.network.NetworkResult
import kotlinx.coroutines.flow.StateFlow

interface CityRepository {
    val locations: StateFlow<List<SavedLocation>>
    suspend fun save(location: SavedLocation)
    suspend fun remove(location: SavedLocation)
    suspend fun geocodeAutocomplete(query: String): NetworkResult<List<ResolvedLocation>>
    suspend fun geocodeReverse(location: Location): NetworkResult<ResolvedLocation>
}
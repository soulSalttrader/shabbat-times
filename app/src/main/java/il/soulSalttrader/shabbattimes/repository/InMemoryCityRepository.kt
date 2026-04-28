package il.soulSalttrader.shabbattimes.repository

import android.location.Location
import android.util.Log
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.di.GeoapifyService
import il.soulSalttrader.shabbattimes.model.Coordinates
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.model.normalize
import il.soulSalttrader.shabbattimes.model.toResolvedLocation
import il.soulSalttrader.shabbattimes.network.NetworkResult
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

@Singleton
class InMemoryCityRepository @Inject constructor(
    private val geoapifyService: GeoapifyService,
    private val dispatcher: CoroutineDispatcher,
) : CityRepository {
    private val _locations: MutableStateFlow<List<SavedLocation>> = MutableStateFlow(emptyList())
    override val locations: StateFlow<List<SavedLocation>> = _locations

    override suspend fun save(location: SavedLocation) {
        _locations.update { current ->
            if (current.any { it.id == location.id }) current
            else current + location
        }
    }

    override suspend fun remove(location: SavedLocation) {
        _locations.update { it.filter { loc -> loc.id != location.id } }
    }

    override suspend fun geocodeAutocomplete(query: String) = withContext(dispatcher) {
        val normalized = query.trim()
        if (normalized.length < 2) {
            return@withContext NetworkResult.Success(emptyList())
        }

        runCatching {
            val response = geoapifyService.api.autocomplete(queryText = normalized)
            if (Debug.enabled) Log.d("InMemoryRepo", "$response")
            response.results?.map { it.toResolvedLocation() } ?: emptyList()
        }.fold(
            onSuccess = { cities -> NetworkResult.Success(data = cities) },
            onFailure = { e -> NetworkResult.Failure(message = "Autocomplete failed: ${e.message}", cause = e.cause) }
        )
    }

    override suspend fun geocodeReverse(location: Location) = withContext(dispatcher) {
        runCatching {
            val response = geoapifyService.api.reverseGeocode(location.latitude, location.longitude)
            response.results?.firstOrNull()?.toResolvedLocation(requestCoordinates = Coordinates(location.latitude, location.longitude).normalize())
                ?: return@withContext NetworkResult.Failure(message = "No results found")
        }.fold(
            onSuccess = { resolved -> NetworkResult.Success(data = resolved) },
            onFailure = { e -> NetworkResult.Failure(message = "Reverse geocode failed: ${e.message}", cause = e.cause) }
        )
    }
}
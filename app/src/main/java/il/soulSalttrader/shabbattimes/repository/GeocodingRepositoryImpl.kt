package il.soulSalttrader.shabbattimes.repository

import android.location.Location
import il.soulSalttrader.shabbattimes.di.GeoapifyService
import il.soulSalttrader.shabbattimes.model.normalize
import il.soulSalttrader.shabbattimes.model.toResolvedLocation
import il.soulSalttrader.shabbattimes.network.NetworkResult
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@Singleton
class GeocodingRepositoryImpl @Inject constructor(
    private val geoapifyService: GeoapifyService,
    private val dispatcher: CoroutineDispatcher,
) : GeocodingRepository {

    override suspend fun reverseGeocode(location: Location) = withContext(dispatcher) {
            runCatching {
                val response = geoapifyService.api.reverseGeocode(location.latitude, location.longitude)
                response.results?.firstOrNull()?.toResolvedLocation(
                    requestCoordinates = location.normalize()
                ) ?: return@withContext NetworkResult.Failure(message = "No results found")
            }.fold(
                onSuccess = { NetworkResult.Success(data = it) },
                onFailure = { e -> NetworkResult.Failure(message = "Reverse geocode failed: ${e.message}", cause = e.cause) }
            )
        }

    override suspend fun autocompleteGeocode(query: String) = withContext(dispatcher) {
            val normalized = query.trim()
            if (normalized.length < 2) {
                return@withContext NetworkResult.Success(emptyList())
            }

            runCatching {
                val response = geoapifyService.api.autocomplete(queryText = normalized)
                response.results?.map { it.toResolvedLocation() } ?: emptyList()
            }.fold(
                onSuccess = { NetworkResult.Success(data = it) },
                onFailure = { e -> NetworkResult.Failure(message = "Autocomplete failed: ${e.message}", cause = e.cause) }
            )
        }
}
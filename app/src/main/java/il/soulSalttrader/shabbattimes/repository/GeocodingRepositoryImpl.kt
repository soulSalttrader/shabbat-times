package il.soulSalttrader.shabbattimes.repository

import android.location.Location
import il.soulSalttrader.shabbattimes.di.GeoapifyService
import il.soulSalttrader.shabbattimes.model.normalize
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.network.dto.toResolvedLocation
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
                ) ?: return@withContext NetworkResult.Failure()
            }.fold(
                onSuccess = { data -> NetworkResult.Success(data) },
                onFailure = { cause -> NetworkResult.Failure(cause) }
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
                onSuccess = { data -> NetworkResult.Success(data) },
                onFailure = { cause -> NetworkResult.Failure(cause) }
            )
        }
}
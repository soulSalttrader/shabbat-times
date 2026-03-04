package il.soulSalttrader.shabbattimes.repository

import android.util.Log
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.di.GeoapifyService
import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.network.dto.toCityDomain
import il.soulSalttrader.shabbattimes.repository.SeedCities.JERUSALEM
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
    private val _cities: MutableStateFlow<List<City>> = MutableStateFlow(listOf(JERUSALEM))
    override val cities: StateFlow<List<City>> = _cities

    override suspend fun addCity(city: City) {
        _cities.update { current ->
            if (current.any { it.id == city.id }) current
            else current + city
        }
    }

    override suspend fun geocodeAutocomplete(query: String) = withContext(dispatcher) {
            val normalized = query.trim()
            if (normalized.length < 2) {
                return@withContext NetworkResult.Success(emptyList())
            }

            runCatching {
                val response = geoapifyService.api.autocomplete(queryText = normalized)
                if (Debug.enabled) Log.d("InMemoryRepo", "$response")
                response.results?.map { it.toCityDomain() } ?: emptyList()
            }.fold(
                onSuccess = { cities -> NetworkResult.Success(data = cities) },
                onFailure = { e -> NetworkResult.Failure(message = "Autocomplete failed: ${e.message}", cause = e.cause) }
            )
        }

    override suspend fun geocodeForward(query: String): NetworkResult<City?> {
        TODO("Not yet implemented")
    }

    override suspend fun geocodeReverse(
        latitude: Double,
        longitude: Double,
    ): NetworkResult<City?> {
        TODO("Not yet implemented")
    }
}
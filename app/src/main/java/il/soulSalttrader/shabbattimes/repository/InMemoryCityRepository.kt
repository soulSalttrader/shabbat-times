package il.soulSalttrader.shabbattimes.repository

import android.util.Log
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.di.GeoapifyService
import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.model.SeedCities.JERUSALEM
import il.soulSalttrader.shabbattimes.network.dto.toCityDomain
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update

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

    override suspend fun geocodeAutocomplete(query: String): Flow<List<City>> = flow {
        if (query.trim().length < 2) {
            emit(emptyList())
            return@flow
        }

        val response = geoapifyService.api.autocomplete(queryText = query.trim())
        if (Debug.enabled) { Log.d("InMemoryRepo", "$response")}

        emit(response.results?.map { it.toCityDomain() } ?: emptyList())
    }.flowOn(dispatcher)
        .catch { emit(emptyList()) }

    override suspend fun geocodeForward(query: String): Flow<City?> {
        TODO("Not yet implemented")
    }

    override suspend fun geocodeReverse(
        latitude: Double,
        longitude: Double,
    ): City? {
        TODO("Not yet implemented")
    }
}
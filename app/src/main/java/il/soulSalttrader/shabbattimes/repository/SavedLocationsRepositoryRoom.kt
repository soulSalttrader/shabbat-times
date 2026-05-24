package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.data.SavedLocationDao
import il.soulSalttrader.shabbattimes.data.toDomain
import il.soulSalttrader.shabbattimes.data.toEntity
import il.soulSalttrader.shabbattimes.di.ApplicationScope
import il.soulSalttrader.shabbattimes.model.SavedLocation
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Singleton
class SavedLocationsRepositoryRoom @Inject constructor(
    private val dao: SavedLocationDao,
    @param:ApplicationScope private val scope: CoroutineScope,
) : SavedLocationsRepository {

    override val locations: StateFlow<List<SavedLocation>> =
        dao.observeAll()
            .map { list -> list.map { it.toDomain() } }
            .stateIn(
                scope = scope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList(),
            )

    override suspend fun save(location: SavedLocation) {
        dao.insertWithOrder(location.toEntity(0))
    }

    override suspend fun reorder(locations: List<SavedLocation>) {
        val entities = locations.mapIndexed { index, loc ->
            loc.toEntity(sortOrder = index)
        }
        dao.upsertAll(entities)
    }

    override suspend fun remove(location: SavedLocation) =
        dao.deleteById(location.id)
}
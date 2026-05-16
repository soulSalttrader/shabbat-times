package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.data.CurrentLocationDao
import il.soulSalttrader.shabbattimes.data.toCurrentLocationEntity
import il.soulSalttrader.shabbattimes.data.toDomain
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
class CurrentLocationRepositoryRoom @Inject constructor(
    private val dao: CurrentLocationDao,
    @param:ApplicationScope private val scope: CoroutineScope,
) : CurrentLocationRepository {

    override val location: StateFlow<SavedLocation?> =
        dao.observe()
            .map { it?.toDomain() }
            .stateIn(
                scope = scope,
                started = SharingStarted.Eagerly,
                initialValue = null,
            )

    override suspend fun update(location: SavedLocation?) {
        if (location == null) dao.clear()
        else dao.upsert(location.toCurrentLocationEntity())
    }
}
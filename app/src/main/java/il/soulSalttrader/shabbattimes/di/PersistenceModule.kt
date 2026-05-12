package il.soulSalttrader.shabbattimes.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import il.soulSalttrader.shabbattimes.repository.CurrentLocationRepository
import il.soulSalttrader.shabbattimes.repository.CurrentLocationRepositoryRoom
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepository
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepositoryRoom
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PersistenceModule {

    @Binds
    @Singleton
    @Persisted
    abstract fun bindSavedLocationsRepo(impl: SavedLocationsRepositoryRoom): SavedLocationsRepository

    @Binds
    @Singleton
    @Persisted
    abstract fun bindCurrentLocationRepo(impl: CurrentLocationRepositoryRoom): CurrentLocationRepository
}
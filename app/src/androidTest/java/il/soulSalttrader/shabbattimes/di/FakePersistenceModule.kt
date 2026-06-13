package il.soulSalttrader.shabbattimes.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import il.soulSalttrader.shabbattimes.repository.CurrentLocationRepository
import il.soulSalttrader.shabbattimes.repository.FakeCurrentLocationRepository
import il.soulSalttrader.shabbattimes.repository.FakeSavedLocationsRepository
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepository
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [PersistenceModule::class]
)
object FakePersistenceModule {
    val fakeSavedLocations = FakeSavedLocationsRepository()
    val fakeCurrentLocation = FakeCurrentLocationRepository()

    @Provides
    @Singleton
    @Persisted
    fun provideSavedLocationsRepository(): SavedLocationsRepository = fakeSavedLocations

    @Provides
    @Singleton
    @Persisted
    fun provideCurrentLocationRepository(): CurrentLocationRepository = fakeCurrentLocation
}
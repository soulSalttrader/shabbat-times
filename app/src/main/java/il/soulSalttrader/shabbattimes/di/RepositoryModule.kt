package il.soulSalttrader.shabbattimes.di

import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import il.soulSalttrader.shabbattimes.repository.CurrentLocationRepository
import il.soulSalttrader.shabbattimes.repository.CurrentLocationRepositoryImpl
import il.soulSalttrader.shabbattimes.repository.GeocodingRepository
import il.soulSalttrader.shabbattimes.repository.GeocodingRepositoryImpl
import il.soulSalttrader.shabbattimes.repository.GpsLocationRepository
import il.soulSalttrader.shabbattimes.repository.GpsLocationRepositoryImpl
import il.soulSalttrader.shabbattimes.repository.PermissionRepository
import il.soulSalttrader.shabbattimes.repository.PermissionRepositoryImpl
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepository
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepositoryInMemory
import il.soulSalttrader.shabbattimes.repository.SolarTimesRepository
import il.soulSalttrader.shabbattimes.repository.SolarTimesRepositoryImpl
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    @InMemory
    abstract fun bindSavedLocationsRepository(impl: SavedLocationsRepositoryInMemory): SavedLocationsRepository

    @Binds
    @Singleton
    abstract fun bindPermissionRepository(impl: PermissionRepositoryImpl): PermissionRepository

    @Binds
    @Singleton
    @InMemory
    abstract fun bindCurrentLocationRepository(impl: CurrentLocationRepositoryImpl): CurrentLocationRepository

    companion object {
        @Provides
        @Singleton
        fun provideGpsLocationRepository(
            fusedClient: FusedLocationProviderClient,
            @ApplicationScope scope: CoroutineScope,
            permissionRepository: PermissionRepository,
        ): GpsLocationRepository =
            GpsLocationRepositoryImpl(fusedClient, scope, permissionRepository)

        @Provides
        @Singleton
        fun provideGeocodingRepository(
            geoapifyService: GeoapifyService,
            dispatcher: CoroutineDispatcher,
        ): GeocodingRepository = GeocodingRepositoryImpl(geoapifyService, dispatcher)

        @Provides
        @Singleton
        fun provideSolarTimesRepository(
            apiService: SolarTimesService,
            dispatcher: CoroutineDispatcher,
        ): SolarTimesRepository = SolarTimesRepositoryImpl(apiService, dispatcher)
    }
}
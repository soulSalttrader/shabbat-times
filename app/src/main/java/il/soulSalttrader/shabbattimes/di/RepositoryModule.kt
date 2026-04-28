package il.soulSalttrader.shabbattimes.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import il.soulSalttrader.shabbattimes.repository.GeocodingRepository
import il.soulSalttrader.shabbattimes.repository.GeocodingRepositoryImpl
import il.soulSalttrader.shabbattimes.repository.GpsLocationRepository
import il.soulSalttrader.shabbattimes.repository.GpsLocationRepositoryImpl
import il.soulSalttrader.shabbattimes.repository.PermissionRepository
import il.soulSalttrader.shabbattimes.repository.PermissionRepositoryImpl
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepository
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepositoryInMemory
import il.soulSalttrader.shabbattimes.repository.ShabbatRepository
import il.soulSalttrader.shabbattimes.repository.ShabbatRepositoryImpl
import il.soulSalttrader.shabbattimes.repository.SolarTimesRepository
import il.soulSalttrader.shabbattimes.repository.SolarTimesRepositoryImpl
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideShabbatRepository(
        dispatcher: CoroutineDispatcher,
        @ApplicationContext context: Context,
    ): ShabbatRepository = ShabbatRepositoryImpl(dispatcher, context)

    @Provides
    @Singleton
    fun provideSavedLocationsRepository(): SavedLocationsRepository = SavedLocationsRepositoryInMemory()

    @Provides
    @Singleton
    fun provideGpsLocationRepository(
        fusedClient: FusedLocationProviderClient,
        @ApplicationScope scope: CoroutineScope,
        permissionRepository: PermissionRepository,
    ): GpsLocationRepository = GpsLocationRepositoryImpl(fusedClient, scope, permissionRepository)

    @Provides
    @Singleton
    fun providePermissionRepository(): PermissionRepository = PermissionRepositoryImpl()

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
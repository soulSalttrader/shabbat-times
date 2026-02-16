package il.soulSalttrader.shabbattimes.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import il.soulSalttrader.shabbattimes.repository.CityRepository
import il.soulSalttrader.shabbattimes.repository.InMemoryCityRepository
import il.soulSalttrader.shabbattimes.repository.ShabbatRepository
import il.soulSalttrader.shabbattimes.repository.ShabbatRepositoryImpl
import il.soulSalttrader.shabbattimes.settings.UserPreferences
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideShabbatRepository(
        apiService: SolarTimesService,
        dispatcher: CoroutineDispatcher,
        userPreferences: UserPreferences,
        @ApplicationContext context: Context,
    ): ShabbatRepository = ShabbatRepositoryImpl(apiService, dispatcher, userPreferences, context)

    @Provides
    @Singleton
    fun provideCityRepository(
        apiService: GeoapifyService,
        dispatcher: CoroutineDispatcher,
    ): CityRepository = InMemoryCityRepository(apiService, dispatcher)
}
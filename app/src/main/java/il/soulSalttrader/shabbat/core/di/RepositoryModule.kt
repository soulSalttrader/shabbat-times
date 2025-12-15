package il.soulSalttrader.retro.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import il.soulSalttrader.retro.shabbatApp.network.ShabbatAPIService
import il.soulSalttrader.retro.shabbatApp.repository.ShabbatRepository
import il.soulSalttrader.retro.shabbatApp.repository.ShabbatRepositoryImpl
import il.soulSalttrader.retro.shabbatApp.settings.UserPreferences
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideShabbatRepository(
        apiService: ShabbatAPIService,
        dispatcher: CoroutineDispatcher,
        userPreferences: UserPreferences,
        @ApplicationContext context: Context,
    ): ShabbatRepository = ShabbatRepositoryImpl(apiService, dispatcher, userPreferences, context)
}
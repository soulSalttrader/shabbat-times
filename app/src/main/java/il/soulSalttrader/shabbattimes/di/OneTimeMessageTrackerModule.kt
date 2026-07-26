package il.soulSalttrader.shabbattimes.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import il.soulSalttrader.shabbattimes.settings.DataStoreOneTimeMessageTracker
import il.soulSalttrader.shabbattimes.settings.OneTimeMessageTracker
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OneTimeMessageTrackerModule {
    @Binds
    @Singleton
    abstract fun bindOneTimeMessageTracker(
        impl: DataStoreOneTimeMessageTracker
    ): OneTimeMessageTracker
}
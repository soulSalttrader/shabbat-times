package il.soulSalttrader.shabbattimes.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.getSystemService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkConnectivityObserverModule {

    @Singleton
    @Provides
    fun provideConnectivityManager(
        @ApplicationContext context: Context,
    ): ConnectivityManager = context.getSystemService<ConnectivityManager>()!!
}
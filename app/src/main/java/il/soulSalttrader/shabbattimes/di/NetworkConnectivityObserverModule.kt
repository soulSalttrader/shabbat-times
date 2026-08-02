package il.soulSalttrader.shabbattimes.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.getSystemService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import il.soulSalttrader.shabbattimes.network.observer.ConnectivityFlowSource
import il.soulSalttrader.shabbattimes.network.observer.ConnectivityManagerFlowSource
import il.soulSalttrader.shabbattimes.network.observer.NetworkConnectivityObserver
import il.soulSalttrader.shabbattimes.network.observer.NetworkObserver
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object NetworkConnectivityObserverModule {

    @Singleton
    @Provides
    fun provideConnectivityManager(
        @ApplicationContext context: Context,
    ): ConnectivityManager = context.getSystemService<ConnectivityManager>()!!

    @Singleton
    @Provides
    fun provideConnectivityFlowSource(
        connectivityManager: ConnectivityManager,
    ): ConnectivityFlowSource = ConnectivityManagerFlowSource(connectivityManager)

    @Singleton
    @Provides
    fun provideNetworkObserver(
        connectivityFlowSource: ConnectivityFlowSource,
        @ApplicationScope scope: CoroutineScope,
    ): NetworkObserver = NetworkConnectivityObserver(connectivityFlowSource, scope)
}

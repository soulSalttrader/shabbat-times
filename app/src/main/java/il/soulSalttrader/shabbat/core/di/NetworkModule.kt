package il.soulSalttrader.retro.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.shabbatApp.network.ApiUrl.BASE_SUNRISE_SUNSET
import il.soulSalttrader.retro.shabbatApp.network.JsonConfig
import il.soulSalttrader.retro.shabbatApp.network.OkHttpClientFactory
import il.soulSalttrader.retro.shabbatApp.network.ShabbatAPIService
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun shabbatAPIService(retrofit: Retrofit): ShabbatAPIService =
        retrofit.create(ShabbatAPIService::class.java)

    @Provides
    @Singleton
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val client = OkHttpClientFactory.create(Debug.enabled)
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(BASE_SUNRISE_SUNSET)
            .client(client)
            .addConverterFactory(JsonConfig.json.asConverterFactory(contentType))
            .build()
    }
}

package il.soulSalttrader.shabbattimes.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.network.ApiUrl.BASE_SUNRISE_SUNSET
import il.soulSalttrader.shabbattimes.network.JsonConfig
import il.soulSalttrader.shabbattimes.network.OkHttpClientFactory
import il.soulSalttrader.shabbattimes.network.SolarTimesApi
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
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun solarTimesService(@SolarTimesRetrofit sunriseRetrofit: Retrofit): SolarTimesApi =
        SolarTimesService(sunriseRetrofit).api

    @Provides
    @Singleton
    @SolarTimesRetrofit
    fun provideSolarTimesRetrofit(): Retrofit {
        val client = OkHttpClientFactory.create(Debug.enabled)
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(BASE_SUNRISE_SUNSET)
            .client(client)
            .addConverterFactory(JsonConfig.json.asConverterFactory(contentType))
            .build()
    }
}

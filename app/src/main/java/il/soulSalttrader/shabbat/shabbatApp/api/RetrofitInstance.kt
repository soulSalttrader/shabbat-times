package il.soulSalttrader.retro.shabbatApp.api

import il.soulSalttrader.retro.core.Debug
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitInstance {
    private val JsonInstance = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (Debug.enabled) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiUrl.BASE_SUNRISE_SUNSET)
            .client(client)
            .addConverterFactory(JsonInstance.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }
}
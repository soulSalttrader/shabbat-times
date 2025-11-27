package il.soulSalttrader.retro.shabbatApp.api

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitInstance {
    private val JsonInstance = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
    }

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiUrl.BASE_SUNRISE_SUNSET)
            .addConverterFactory(JsonInstance.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }
}
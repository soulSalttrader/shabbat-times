package il.soulSalttrader.retro.shabbatApp.network

import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.shabbatApp.network.ApiUrl.BASE_SUNRISE_SUNSET
import il.soulSalttrader.retro.shabbatApp.network.JsonConfig.json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient {
    private val contentType = "application/json".toMediaType()
    private val client = OkHttpClientFactory.create(Debug.enabled)

    val shabbatApi: ShabbatAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_SUNRISE_SUNSET)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(ShabbatAPIService::class.java)
    }
}
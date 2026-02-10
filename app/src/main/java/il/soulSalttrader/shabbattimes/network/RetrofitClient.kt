package il.soulSalttrader.shabbattimes.network

import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.network.ApiUrl.BASE_SUNRISE_SUNSET
import il.soulSalttrader.shabbattimes.network.JsonConfig.json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient {
    private val contentType = "application/json".toMediaType()
    private val client = OkHttpClientFactory.create(Debug.enabled)

    val shabbatApi: SolarTimesApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_SUNRISE_SUNSET)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(SolarTimesApi::class.java)
    }
}
package il.soulSalttrader.shabbattimes.di

import il.soulSalttrader.shabbattimes.network.SolarTimesApi
import jakarta.inject.Inject
import jakarta.inject.Singleton
import retrofit2.Retrofit

@Singleton
class SolarTimesService @Inject constructor(
    @param:SolarTimesRetrofit private val retrofit: Retrofit
) {
    val api: SolarTimesApi by lazy { retrofit.create(SolarTimesApi::class.java) }
}
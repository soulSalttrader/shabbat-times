package il.soulSalttrader.shabbattimes.di

import il.soulSalttrader.shabbattimes.network.GeoapifyApi
import jakarta.inject.Inject
import jakarta.inject.Singleton
import retrofit2.Retrofit
import kotlin.getValue

@Singleton
class GeoapifyService @Inject constructor(
    @param:GeoapifyRetrofit private val retrofit: Retrofit
) {
    val api: GeoapifyApi by lazy { retrofit.create(GeoapifyApi::class.java) }
}
package il.soulSalttrader.retro.shabbatApp.network

import il.soulSalttrader.retro.shabbatApp.model.Cities
import il.soulSalttrader.retro.shabbatApp.network.dto.SolarTimesDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ShabbatAPIService {
    @GET("json")
    suspend fun getSolarTimes(
        @Query("lat") lat: Double = Cities.JERUSALEM.lat,
        @Query("lng") lng: Double = Cities.JERUSALEM.lng,
        @Query("timezone") timezone: String = Cities.JERUSALEM.timezone,
        @Query("time_format") timeFormat: Int = 24,
        @Query("date") date: String? = null,
    ): SolarTimesDto
}
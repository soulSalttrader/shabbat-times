package il.soulSalttrader.shabbattimes.network

import il.soulSalttrader.shabbattimes.model.SeedCities
import il.soulSalttrader.shabbattimes.network.dto.SolarTimesDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ShabbatAPIService {
    @GET("json")
    suspend fun getSolarTimes(
        @Query("lat") lat: Double = SeedCities.JERUSALEM.coordinates.latitude,
        @Query("lng") lng: Double = SeedCities.JERUSALEM.coordinates.longitude,
        @Query("timezone") timezone: String = SeedCities.JERUSALEM.timeZone.id,
        @Query("time_format") timeFormat: Int = 24,
        @Query("date") date: String? = null,
    ): SolarTimesDto
}
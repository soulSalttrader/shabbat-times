package il.soulSalttrader.shabbattimes.network

import il.soulSalttrader.shabbattimes.model.Coordinates
import il.soulSalttrader.shabbattimes.network.dto.SolarTimesResponseDto
import il.soulSalttrader.shabbattimes.settings.UserPreferences
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.ZoneId

interface SolarTimesApi {
    @GET("json")
    suspend fun getSolarTimes(
        @Query("lat") lat: Double = Coordinates.EMPTY.latitude,
        @Query("lng") lng: Double = Coordinates.EMPTY.longitude,
        @Query("timezone") timezone: String = ZoneId.systemDefault().id,
        @Query("time_format") timeFormat: Int = UserPreferences.DEFAULT_TIME_FORMAT,
        @Query("date") date: String? = null,
    ): SolarTimesResponseDto
}
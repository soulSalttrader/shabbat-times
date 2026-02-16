package il.soulSalttrader.shabbattimes.network

import il.soulSalttrader.shabbattimes.BuildConfig
import il.soulSalttrader.shabbattimes.network.dto.GeoapifyResponseDto
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale

interface GeoapifyApi {
    @GET("autocomplete")
    suspend fun autocomplete(
        @Query("text") queryText: String,
        @Query("filter") countryFilter: String? = null,
//        @Query("bias") locationBias: String? = "ipstack",
        @Query("type") resultType: String? = "city",

        @Query("limit") maxResults: Int = 5,
        @Query("lang") preferredLanguage: String = Locale.getDefault().language,
        @Query("format") format: String = "json",
        @Query("apiKey") apiKey: String = BuildConfig.GEOAPIFY_API_KEY,
    ): GeoapifyResponseDto

    @GET("search")
    suspend fun forwardSearch(
        @Query("text") queryText: String,
        @Query("filter") countryFilter: String? = null,
//        @Query("bias") locationBias: String? = "ipstack",
        @Query("type") resultType: String? = "city",

        @Query("limit") maxResults: Int = 5,
        @Query("lang") preferredLanguage: String = Locale.getDefault().language,
        @Query("format") format: String = "json",
        @Query("apiKey") apiKey: String = BuildConfig.GEOAPIFY_API_KEY,
    ): GeoapifyResponseDto

    @GET("reverse")
    suspend fun reverseGeocode(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,

        @Query("limit") maxResults: Int = 1,
        @Query("lang") preferredLanguage: String = Locale.getDefault().language,
        @Query("format") format: String = "json",
        @Query("apiKey") apiKey: String = BuildConfig.GEOAPIFY_API_KEY,
    ): GeoapifyResponseDto
}
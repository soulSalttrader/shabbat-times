# Solar Times API

The app uses the public API at https://sunrisesunset.io/ to fetch daily solar times (primarily sunset, nautical_twilight_end, and utc_offset).

## Example API response

The API returns a comprehensive set of solar and twilight events in a single JSON object.

```kotlin
{
  "results": {
    "date": "2026-04-04",
    "sunrise": "6:48:29 AM",
    "sunset": "7:36:01 PM",
    "first_light": "5:16:33 AM",
    "last_light": "9:07:57 PM",
    "dawn": "6:21:29 AM",
    "dusk": "8:03:01 PM",
    "solar_noon": "1:12:15 PM",
    "golden_hour": "7:00:44 PM",
    "day_length": "12:47:32",
    "nautical_twilight_begin": "5:49:31 AM",
    "nautical_twilight_end": "8:34:59 PM",
    "timezone": "America/New_York",
    "utc_offset": -240,
    "sun_altitude": 56.81,
    "sun_azimuth": 180.33,
    "sunrise_azimuth": 82,
    "sunset_azimuth": 278.38,
    "moonrise": "9:38:28 PM",
    "moonset": "7:48:47 AM",
    "moon_illumination": 92.7,
    "moon_phase": "Waning Gibbous",
    "moon_phase_value": 0.59,
    "moon_always_up": false,
    "moon_always_down": false,
    "elevation": 26
  },
  "status": "OK",
  "tzid": "America/New_York"
}
```

## Retrofit endpoint definition

The `SolarTimesApi` and `GeoapifyApi` interfaces define the Retrofit endpoints for network requests.

The SolarTimesApi interface defines the Retrofit endpoints for solar time fetching.

```kotlin
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
```

The GeoapifyApi interface defines the Retrofit endpoints for location autocomplete, forward search and reverse geocoding.

```kotlin
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
```

## DTO and domain mapping

DTOs map the external JSON structure to internal Kotlin types before they are converted to domain models.

```kotlin
@Serializable
data class SolarTimesResponseDto(
    val results: SolarTimesResultDto = SolarTimesResultDto(),
    val status: String = "",
)

@Serializable
data class SolarTimesResultDto(
  val date: String = "",
  val sunset: String = "",
  val dusk: String = "",
  @SerialName("nautical_twilight_end") val nauticalTwilightEnd: String = "",
  @SerialName("utc_offset") val utcOffsetMinutes: Int = 0,
)

@Serializable
data class GeoapifyResponseDto(
    val results: List<GeoapifyResultDto>? = null,
    val status: String? = null,
    val query: GeoapifyQuery? = null
)

@Serializable
data class GeoapifyResultDto(
    @SerialName("lat") val latitude: Double? = null,
    @SerialName("lon") val longitude: Double? = null,
    val timezone: GeoapifyTimezone? = null,
// ...
)
```

## `GetHalachicTimesUseCase` fetches specific solar times for
- upcoming Friday → used to compute candle lighting
- upcoming Saturday → used to compute Havdalah

Dates are provided by `ShabbatCalendar` — injected and testable:
```kotlin
interface ShabbatCalendar {
    fun upcomingCandleLightingDate(): LocalDate
    fun upcomingHavdalahDate(): LocalDate
}
```

`SolarTimesRepository` only fetches solar times for a given `SolarTimesRequest` — it has no knowledge of Shabbat, candle lighting, or havdalah concepts.

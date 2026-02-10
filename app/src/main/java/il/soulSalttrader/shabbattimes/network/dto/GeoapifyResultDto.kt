package il.soulSalttrader.shabbattimes.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a single geocoding result from Geoapify (forward, reverse, and autocomplete).
 *
 * Important notes:
 * - The same DTO is used for forward search (`/search`), reverse (`/reverse`), autocomplete ('/autocomplete') and .
 * - Not all fields are present in every response type.
 * - `result_type` is the most reliable way to understand what kind of place this is.
 *
 * Common result_type values:
 * - "city"          → administrative city / town
 * - "postcode"      → postal code area
 * - "street"        → street level
 * - "amenity"       → POI (restaurant, museum, etc.)
 * - "building"      → specific address with housenumber
 *
 * Examples (real Geoapify responses, simplified):
 *
 * Forward search – city level:
 * {
 *   country: "Czechia"
 *   country_code: "cz"
 *   state: "Southeast"
 *   county: "South Moravian Region"
 *   city: "Brno"
 *   lon: 16.6113382
 *   lat: 49.1922443
 *   result_type: "city"
 *   formatted: "Brno, Southeast, Czechia"
 *   timezone: { name: "Europe/Prague", offset_STD: "+01:00", offset_STD_seconds: 3600 }
 *   rank: { importance: 0.6946045940930433, popularity: 8.959839141499655, confidence: 1, confidence_city_level: 1 }
 * }
 *
 * Reverse geocoding – street level:
 * {
 *   country: "Israel"
 *   country_code: "il"
 *   state: "Jerusalem"
 *   county: "Jerusalem"
 *   street: "Western Wall Plaza"
 *   city: "Jerusalem"
 *   lon: 35.233992
 *   lat: 31.776412
 *   result_type: "amenity"
 *   formatted: "Western Wall Plaza, 9114101 Jerusalem, Israel"
 *   state_code: "JM"
 *   county_code: "JM"
 *   suburb: "Old City"
 *   distance: 21.914721970930717
 *   postcode: "9114101"
 * }
 *
 * Autocomplete geocoding - typed: "New ":
 * {
 *    { country: "United States",  country_code: "us", state: "New York",   city: "New York" }
 *    { country: "United Kingdom", country_code: "gb", state: "Wales",      city: "Newport" }
 *    { country: "United Kingdom", country_code: "gb", state: "England",    city: "Newcastle upon Tyne" }
 *    { country: "United States",  country_code: "us", state: "Louisiana",  city: "New Orleans" }
 *    { country: "United States",  country_code: "us", state: "New Jersey", city: "Newark" }
 * }
 */
@Serializable
data class GeoapifyResultDto(
    // ────────────────────────────────────────────────
    // Core location
    // ────────────────────────────────────────────────
    @SerialName("lat")          val latitude: Double? = null,
    @SerialName("lon")          val longitude: Double? = null,
    val timezone: GeoapifyTimezone? = null, // "America/New_York"

    // ────────────────────────────────────────────────
    // Address components
    // ────────────────────────────────────────────────
    @SerialName("country")      val countryName: String? = null,   // "United States"
    @SerialName("country_code") val countryCode: String? = null,   // "us"
    @SerialName("state")        val stateOrRegion: String? = null, // "New York"
    @SerialName("state_code")   val stateCode: String? = null,     // "NY"
    @SerialName("city")         val cityName: String? = null,      // "New York"
    @SerialName("housenumber")  val houseNumber: String? = null,
    @SerialName("formatted")    val fullAddress: String? = null,   // "7th Avenue, New York, NY 10014, United States of America"
    @SerialName("name")         val placeName: String? = null,     // "7th Avenue South"
    @SerialName("place_id")     val placeId: String? = null,
    val county: String? = null,     // "New York County"
    val postcode: String? = null,   // "10014"
    val suburb: String? = null,     // "Manhattan"
    val quarter: String? = null,
    val street: String? = null,     // "7th Avenue South"

    // ────────────────────────────────────────────────
    // Metadata & quality indicators
    // ────────────────────────────────────────────────
    @SerialName("result_type") val resultType: String? = null, // "street" ("city", "building", ..., "amenity")
    val rank: GeoapifyRank? = null,
    val datasource: GeoapifyDatasource? = null,
)
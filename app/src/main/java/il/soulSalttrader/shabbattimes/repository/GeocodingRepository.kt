package il.soulSalttrader.shabbattimes.repository

import android.location.Location
import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.network.NetworkResult

interface GeocodingRepository {
    suspend fun reverseGeocode(location: Location): NetworkResult<ResolvedLocation>
    suspend fun autocompleteGeocode(query: String): NetworkResult<List<ResolvedLocation>>
}
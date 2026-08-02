package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.model.Coordinates
import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.network.NetworkResult

interface GeocodingRepository {
    suspend fun reverseGeocode(coordinates: Coordinates): NetworkResult<ResolvedLocation>
    suspend fun autocompleteGeocode(query: String): NetworkResult<List<ResolvedLocation>>
}

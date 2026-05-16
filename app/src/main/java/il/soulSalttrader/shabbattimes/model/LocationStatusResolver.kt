package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.model.LocationPermission.*

fun resolveLocationStatus(
    location: SavedLocation,
    currentLocation: SavedLocation?,
    permission: LocationPermission,
): LocationStatus {
    val distanceKm = currentLocation?.coordinates?.distanceTo(location.coordinates)
    val isGps = location.id == SavedLocation.GPS_ID
    val hasCurrentLocation = currentLocation != null
    val hasPermission = permission is Granted

    return when {
        isGps && !hasCurrentLocation    -> LocationStatus.LastKnownLocation
        isGps && !hasPermission         -> LocationStatus.LastKnownLocation
        permission is Denied            -> LocationStatus.NoPermission
        permission is DeniedPermanently -> LocationStatus.NoPermission
        permission is Requesting        -> LocationStatus.Locating
        isGps                           -> LocationStatus.Current
        distanceKm == null              -> LocationStatus.Unknown
        distanceKm < 0.1                -> LocationStatus.Current
        else                            -> LocationStatus.Nearby(distanceKm)
    }
}
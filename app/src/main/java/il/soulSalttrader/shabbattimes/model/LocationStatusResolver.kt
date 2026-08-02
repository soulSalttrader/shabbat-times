package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.model.LocationPermission.Denied
import il.soulSalttrader.shabbattimes.model.LocationPermission.DeniedPermanently
import il.soulSalttrader.shabbattimes.model.LocationPermission.Requesting

fun resolveLocationStatus(
    location: SavedLocation,
    currentLocation: CurrentLocationState,
    permission: LocationPermission,
): LocationStatus {
    val isGps = location.id == SavedLocation.GPS_ID

    return when {
        permission is Denied -> LocationStatus.NoPermission
        permission is DeniedPermanently -> LocationStatus.NoPermission
        permission is Requesting -> LocationStatus.Locating

        currentLocation is CurrentLocationState.Fetching -> LocationStatus.Locating

        isGps && currentLocation is CurrentLocationState.Idle -> LocationStatus.LastKnownLocation
        isGps && currentLocation is CurrentLocationState.Available -> LocationStatus.Current

        currentLocation is CurrentLocationState.Idle -> LocationStatus.Unknown

        currentLocation is CurrentLocationState.Available -> {
            val distanceKm = currentLocation.coordinates.distanceTo(location.coordinates)
            when {
                distanceKm < 0.1 -> LocationStatus.Current
                else -> LocationStatus.Nearby(distanceKm)
            }
        }

        else -> LocationStatus.Unknown
    }
}

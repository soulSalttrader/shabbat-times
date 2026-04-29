package il.soulSalttrader.shabbattimes.event

import il.soulSalttrader.shabbattimes.location.GpsState
import il.soulSalttrader.shabbattimes.location.LocationPermission
import il.soulSalttrader.shabbattimes.location.LocationStatus
import il.soulSalttrader.shabbattimes.location.LocationUiModel
import il.soulSalttrader.shabbattimes.location.LocationUiState
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.model.distanceTo
import il.soulSalttrader.shabbattimes.reducer.LocationReducer
import il.soulSalttrader.shabbattimes.reducer.Reducible

sealed interface LocationEvent : AppEvent, Reducible<LocationUiState> {
    data class LocationDeleted(val savedLocation: SavedLocation, val isCurrent: Boolean) : LocationEvent {
        override val reducer = LocationReducer { state -> state } // The reducer is a no-op because the repository flow handles the UI update reactively
    }

    data class LocationLoaded(
        val currentLocation: LocationUiModel?,
        val savedLocations: List<SavedLocation>,
    ) : LocationEvent {
        override val reducer = LocationReducer { state ->
            val gpsCoordinates = currentLocation?.location?.coordinates

            state.copy(
                currentLocation = currentLocation,
                savedLocations = savedLocations.map { location ->
                    val distanceKm = gpsCoordinates?.distanceTo(location.coordinates)
                    LocationUiModel(
                        location = location,
                        status = when {
                            distanceKm == null -> LocationStatus.Unknown
                            distanceKm < 0.1   -> LocationStatus.Current
                            else               -> LocationStatus.Nearby(distanceKm)
                        },
                    )
                },
                gpsState = currentLocation?.let { GpsState.Ready } ?: GpsState.Idle
            )
        }
    }

    data class GpsLocationError(val message: String) : LocationEvent {
        override val reducer = LocationReducer { state ->
            state.copy(gpsState = GpsState.Error(message))
        }
    }

    data object GpsLocationRequested : LocationEvent {
        override val reducer = LocationReducer { state ->
            state.copy(gpsState = GpsState.Loading)
        }
    }

    data object GpsPermissionDenied : LocationEvent {
        override val reducer = LocationReducer { state ->
            state.copy(gpsState = GpsState.NoPermission)
        }
    }

    data object GpsLocationIdle : LocationEvent {
        override val reducer = LocationReducer { state ->
            state.copy(gpsState = GpsState.Idle)
        }
    }

    data class GpsPermissionChanged(val permission: LocationPermission) : LocationEvent {
        override val reducer = LocationReducer { state ->
            state.copy(
                gpsState = when (permission) {
                    is LocationPermission.Idle              -> GpsState.Idle
                    is LocationPermission.Requesting        -> GpsState.Loading
                    is LocationPermission.Denied,
                    is LocationPermission.DeniedPermanently -> GpsState.NoPermission
                    else                                    -> state.gpsState
                }
            )
        }
    }
}
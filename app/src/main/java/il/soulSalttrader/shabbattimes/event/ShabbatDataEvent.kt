package il.soulSalttrader.shabbattimes.event

import android.util.Log
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.content.shabbat.ShabbatResultState
import il.soulSalttrader.shabbattimes.content.shabbat.ShabbatUiState
import il.soulSalttrader.shabbattimes.location.GpsState
import il.soulSalttrader.shabbattimes.location.LocationPermission
import il.soulSalttrader.shabbattimes.location.LocationStatus
import il.soulSalttrader.shabbattimes.model.HalachicTimes
import il.soulSalttrader.shabbattimes.model.LocationWithTimes
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.model.distanceTo
import il.soulSalttrader.shabbattimes.model.toDisplay
import il.soulSalttrader.shabbattimes.reducer.Reducible
import il.soulSalttrader.shabbattimes.reducer.ShabbatReducer
import kotlinx.collections.immutable.toImmutableList

sealed interface ShabbatDataEvent : AppEvent, Reducible<ShabbatUiState> {
    data class LocationWithTimesLoaded(
        val savedLocations: List<SavedLocation>,
        val halachicTimes: List<HalachicTimes>,
        val gpsLocation: SavedLocation?,
    ) : ShabbatDataEvent {
        override val reducer = ShabbatReducer { state ->
            val allSavedLocations = buildList {
                gpsLocation?.let { add(it) }
                addAll(savedLocations)
            }

            val savedLocationWithTimes = allSavedLocations.map { location ->
                val distanceKm = gpsLocation?.coordinates?.distanceTo(location.coordinates)
                LocationWithTimes(
                    location = location,
                    times = halachicTimes
                        .firstOrNull { it.coordinates == location.coordinates }
                        ?.toDisplay(),
                    status = when {
                        distanceKm == null -> LocationStatus.Unknown
                        distanceKm < 0.1   -> LocationStatus.Current
                        else               -> LocationStatus.Nearby(distanceKm)
                    },
                )
            }

            state.copy(
                data = when {
                    allSavedLocations.isEmpty() -> ShabbatResultState.NoResults
                    else                     -> ShabbatResultState.Results(savedLocationWithTimes.toImmutableList())
                }
            )
        }
    }

    data object RetryLoadLocationWithTimes : ShabbatDataEvent {
        override val reducer = ShabbatReducer { state -> state }
    }

    data object LoadLocationWithTimes : ShabbatDataEvent {
        override val reducer = ShabbatReducer { state ->
            state.copy(data = ShabbatResultState.Loading)
        }
    }
    class LocationWithTimesLoadFailed(val message: String, val cause: Throwable?) : ShabbatDataEvent {
        override val reducer = ShabbatReducer { state ->
            if (Debug.enabled) Log.d("ShabbatEvent", "message: $message, cause: $cause")
            state.copy(data = ShabbatResultState.Failure(message, cause))
        }
    }

    data class LocationDeleted(val savedLocation: SavedLocation, val isCurrent: Boolean) : ShabbatDataEvent {
        override val reducer = ShabbatReducer { state -> state } // The reducer is a no-op because the repository flow handles the UI update reactively
    }

    data class GpsLocationError(val message: String) : ShabbatDataEvent {
        override val reducer = ShabbatReducer { state ->
            state.copy(gpsState = GpsState.Error(message))
        }
    }

    data object GpsLocationRequested : ShabbatDataEvent {
        override val reducer = ShabbatReducer { state ->
            state.copy(gpsState = GpsState.Loading)
        }
    }

    data object GpsPermissionDenied : ShabbatDataEvent {
        override val reducer = ShabbatReducer { state ->
            state.copy(gpsState = GpsState.NoPermission)
        }
    }

    data object GpsLocationIdle : ShabbatDataEvent {
        override val reducer = ShabbatReducer { state ->
            state.copy(gpsState = GpsState.Idle)
        }
    }

    data class GpsPermissionChanged(val permission: LocationPermission) : ShabbatDataEvent {
        override val reducer = ShabbatReducer { state ->
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
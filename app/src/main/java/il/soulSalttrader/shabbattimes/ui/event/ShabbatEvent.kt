package il.soulSalttrader.shabbattimes.ui.event

import android.util.Log
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.ui.shabbat.ShabbatResultState
import il.soulSalttrader.shabbattimes.ui.shabbat.ShabbatUiState
import il.soulSalttrader.shabbattimes.location.LocationPermission
import il.soulSalttrader.shabbattimes.location.LocationPermission.*
import il.soulSalttrader.shabbattimes.location.LocationStatus
import il.soulSalttrader.shabbattimes.model.HalachicTimes
import il.soulSalttrader.shabbattimes.model.ShabbatEntry
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.model.distanceTo
import il.soulSalttrader.shabbattimes.model.toDisplay
import il.soulSalttrader.shabbattimes.ui.reducer.Reducible
import il.soulSalttrader.shabbattimes.ui.reducer.ShabbatReducer
import kotlinx.collections.immutable.toImmutableList

sealed interface ShabbatEvent : AppEvent, Reducible<ShabbatUiState> {
    data class ShabbatEntryLoaded(
        val savedLocations: List<SavedLocation>,
        val currentLocation: SavedLocation?,
        val halachicTimes: List<HalachicTimes>,
        val permission: LocationPermission,
    ) : ShabbatEvent {
        override val reducer = ShabbatReducer { state ->
            val availableLocations = buildList {
                currentLocation?.let { add(it) }
                addAll(savedLocations)
            }.toImmutableList()

            val shabbatEntries = availableLocations.map { location ->
                val distanceKm = currentLocation?.coordinates?.distanceTo(location.coordinates)
                ShabbatEntry(
                    location = location,
                    times = halachicTimes
                        .firstOrNull { it.coordinates == location.coordinates }
                        ?.toDisplay(),
                    status = when {
                        permission is Denied            -> LocationStatus.NoPermission
                        permission is DeniedPermanently -> LocationStatus.NoPermission
                        permission is Requesting        -> LocationStatus.Locating
                        distanceKm == null              -> LocationStatus.Unknown
                        distanceKm < 0.1                -> LocationStatus.Current
                        else                            -> LocationStatus.Nearby(distanceKm)
                    },
                )
            }.toImmutableList()

            state.copy(
                shabbat = when {
                    availableLocations.isEmpty() -> ShabbatResultState.Empty
                    else                         -> ShabbatResultState.Ready(shabbatEntries)
                }
            )
        }
    }

    data object RetryLoadShabbatEntry : ShabbatEvent {
        override val reducer = ShabbatReducer { state -> state }
    }

    class ShabbatEntryLoadFailed(val message: String, val cause: Throwable?) : ShabbatEvent {
        override val reducer = ShabbatReducer { state ->
            if (Debug.enabled) Log.d("ShabbatEvent", "message: $message, cause: $cause")
            state.copy(shabbat = ShabbatResultState.Failure(message, cause))
        }
    }

    data class LocationDeleted(val savedLocation: SavedLocation, val isCurrent: Boolean) : ShabbatEvent {
        override val reducer = ShabbatReducer { state -> state } // The reducer is a no-op because the repository flow handles the UI update reactively
    }
}
package il.soulSalttrader.shabbattimes.event

import android.util.Log
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.content.shabbat.ShabbatResultState
import il.soulSalttrader.shabbattimes.content.shabbat.ShabbatUiState
import il.soulSalttrader.shabbattimes.location.LocationStatus
import il.soulSalttrader.shabbattimes.model.HalachicTimes
import il.soulSalttrader.shabbattimes.model.LocationWithTimes
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.model.distanceTo
import il.soulSalttrader.shabbattimes.model.toDisplay
import il.soulSalttrader.shabbattimes.reducer.Reducible
import il.soulSalttrader.shabbattimes.reducer.ShabbatReducer
import kotlinx.collections.immutable.toImmutableList

sealed interface ShabbatEvent : AppEvent, Reducible<ShabbatUiState> {
    data class LocationWithTimesLoaded(
        val savedLocations: List<SavedLocation>,
        val currentLocation: SavedLocation?,
        val halachicTimes: List<HalachicTimes>,
    ) : ShabbatEvent {
        override val reducer = ShabbatReducer { state ->
            val availableLocations = buildList {
                currentLocation?.let { add(it) }
                addAll(savedLocations)
            }.toImmutableList()

            val savedLocationWithTimes = availableLocations.map { location ->
                val distanceKm = currentLocation?.coordinates?.distanceTo(location.coordinates)
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
            }.toImmutableList()

            state.copy(
                data = when {
                    availableLocations.isEmpty() -> ShabbatResultState.NoResults
                    else                         -> ShabbatResultState.Results(savedLocationWithTimes)
                }
            )
        }
    }

    data object RetryLoadLocationWithTimes : ShabbatEvent {
        override val reducer = ShabbatReducer { state -> state }
    }

    class LocationWithTimesLoadFailed(val message: String, val cause: Throwable?) : ShabbatEvent {
        override val reducer = ShabbatReducer { state ->
            if (Debug.enabled) Log.d("ShabbatEvent", "message: $message, cause: $cause")
            state.copy(data = ShabbatResultState.Failure(message, cause))
        }
    }

    data class LocationDeleted(val savedLocation: SavedLocation, val isCurrent: Boolean) : ShabbatEvent {
        override val reducer = ShabbatReducer { state -> state } // The reducer is a no-op because the repository flow handles the UI update reactively
    }
}
package il.soulSalttrader.shabbattimes.ui.event

import android.util.Log
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.model.HalachicTimes
import il.soulSalttrader.shabbattimes.model.LocationPermission
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.model.ShabbatEntry
import il.soulSalttrader.shabbattimes.model.findForLocation
import il.soulSalttrader.shabbattimes.model.resolveLocationStatus
import il.soulSalttrader.shabbattimes.model.toDisplay
import il.soulSalttrader.shabbattimes.ui.reducer.Reducible
import il.soulSalttrader.shabbattimes.ui.reducer.ShabbatReducer
import il.soulSalttrader.shabbattimes.ui.shabbat.ShabbatResultState
import il.soulSalttrader.shabbattimes.ui.shabbat.ShabbatUiState
import kotlinx.collections.immutable.toImmutableList

sealed interface ShabbatEvent : AppEvent, Reducible<ShabbatUiState> {
    data class ShabbatEntryLoaded(
        val savedLocations: List<SavedLocation>,
        val currentLocation: SavedLocation?,
        val halachicTimes: List<HalachicTimes>,
        val permission: LocationPermission,
    ) : ShabbatEvent {
        override val reducer = ShabbatReducer { state ->
            val shabbatEntries = savedLocations.map { location ->
                ShabbatEntry(
                    location = location,
                    times = halachicTimes.findForLocation(location)?.toDisplay(),
                    status = resolveLocationStatus(
                        location = location,
                        currentLocation = currentLocation,
                        permission = permission,
                    ),
                )
            }.toImmutableList()

            state.copy(
                shabbat = when {
                    savedLocations.isEmpty() -> ShabbatResultState.Empty
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

    data class ReorderLocations(val from: Int, val to: Int) : ShabbatEvent {
        override val reducer = ShabbatReducer { state -> state } // The reducer is a no-op because order is persisted to the repository, which triggers ShabbatEntryLoaded to rebuild entries in the correct order reactively
    }
}
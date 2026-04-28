package il.soulSalttrader.shabbattimes.event

import android.util.Log
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.content.shabbat.ShabbatResultState
import il.soulSalttrader.shabbattimes.content.shabbat.ShabbatUiState
import il.soulSalttrader.shabbattimes.model.HalachicTimes
import il.soulSalttrader.shabbattimes.model.LocationWithTimes
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.model.toDisplay
import il.soulSalttrader.shabbattimes.reducer.Reducible
import il.soulSalttrader.shabbattimes.reducer.ShabbatReducer

sealed interface ShabbatDataEvent : AppEvent, Reducible<ShabbatUiState> {
    data class LocationWithTimesLoaded(
        val savedLocations: List<SavedLocation>,
        val halachicTimes: List<HalachicTimes>,
    ) : ShabbatDataEvent {
        override val reducer = ShabbatReducer { state ->
            if (Debug.enabled) Log.d("ShabbatEvent", "$savedLocations")

            val locationWithTimes = savedLocations.map { location ->
                LocationWithTimes(
                    location = location,
                    times = halachicTimes
                        .firstOrNull { it.coordinates == location.coordinates }
                        ?.toDisplay()
                )
            }

            state.copy(
                data = when {
                    savedLocations.isEmpty() -> ShabbatResultState.NoResults
                    halachicTimes.isEmpty()  -> ShabbatResultState.Loading
                    else                     -> ShabbatResultState.Results(data = locationWithTimes)
                },
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
}
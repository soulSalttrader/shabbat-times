package il.soulSalttrader.shabbattimes.event

import android.util.Log
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay
import il.soulSalttrader.shabbattimes.content.shabbat.ShabbatResultState
import il.soulSalttrader.shabbattimes.content.shabbat.ShabbatUiState
import il.soulSalttrader.shabbattimes.reducer.Reducible
import il.soulSalttrader.shabbattimes.reducer.ShabbatReducer

sealed interface ShabbatDataEvent : AppEvent, Reducible<ShabbatUiState> {
    data object LoadTimes : ShabbatDataEvent {
        override val reducer = ShabbatReducer { state ->
            state.copy(data = ShabbatResultState.Loading)
        }
    }
    class TimesLoadFailed(val message: String, val cause: Throwable?) : ShabbatDataEvent {
        override val reducer = ShabbatReducer { state ->
            if (Debug.enabled) Log.d("ShabbatEvent", "message: $message, cause: $cause")
            state.copy(data = ShabbatResultState.Failure(message, cause))
        }
    }

    data class TimesLoaded(val times: List<HalachicTimesDisplay>) : ShabbatDataEvent {
        override val reducer = ShabbatReducer { state ->
            if (Debug.enabled) Log.d("ShabbatEvent", "$times")
            state.copy(
                data = when {
                    times.isEmpty() -> ShabbatResultState.NoResults
                    else            -> ShabbatResultState.Results(data = times)
                },
            )
        }
    }

    data object RetryLoadTimes : ShabbatDataEvent {
        override val reducer = ShabbatReducer { state -> state }
    }

    data object RefreshTimes : ShabbatDataEvent {
        override val reducer = ShabbatReducer { state ->
            state.copy(data = ShabbatResultState.Loading)
        }
    }

    data object ClearTimes : ShabbatDataEvent {
        override val reducer = ShabbatReducer { state ->
            state.copy(data = ShabbatResultState.Idle)
        }
    }
}
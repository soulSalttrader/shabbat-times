package il.soulSalttrader.shabbattimes.event

import android.util.Log
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay
import il.soulSalttrader.shabbattimes.model.ShabbatDataState
import il.soulSalttrader.shabbattimes.model.ShabbatUiState
import il.soulSalttrader.shabbattimes.reducer.Reducible
import il.soulSalttrader.shabbattimes.reducer.ShabbatReducer

sealed interface ShabbatDataEvent : AppEvent, Reducible<ShabbatUiState> {
    data object Load : ShabbatDataEvent {
        override val reducer = ShabbatReducer { state ->
            state.copy(data = ShabbatDataState.Loading)
        }
    }

    sealed interface Loaded : ShabbatDataEvent {
        class Success(val display: List<HalachicTimesDisplay>) : Loaded {
            override val reducer = ShabbatReducer { state ->
                if (Debug.enabled) Log.d("ShabbatEvent.Loaded.Success", "$display")

                state.copy(data = ShabbatDataState.Success(display))
            }
        }

        class Failure(val message: String, val cause: Throwable?) : Loaded {
            override val reducer = ShabbatReducer { state ->
                if (Debug.enabled) Log.d(
                    "ShabbatEvent.Loaded.Failure",
                    "message: $message, cause: $cause"
                )

                state.copy(data = ShabbatDataState.Failure(message, cause))
            }
        }
    }
}
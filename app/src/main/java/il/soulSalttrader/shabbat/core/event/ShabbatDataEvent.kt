package il.soulSalttrader.retro.core.event

import android.util.Log
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.core.reducer.Reducible
import il.soulSalttrader.retro.core.reducer.ShabbatReducer
import il.soulSalttrader.retro.shabbatApp.model.HalachicTimesDisplay
import il.soulSalttrader.retro.shabbatApp.model.ShabbatDataState
import il.soulSalttrader.retro.shabbatApp.model.ShabbatState

sealed interface ShabbatDataEvent : AppEvent, Reducible<ShabbatState> {
    data object Load : ShabbatDataEvent {
        override val reducer = ShabbatReducer { state ->
            state.copy(data = ShabbatDataState.Loading)
        }
    }

    sealed interface Loaded : ShabbatDataEvent {
        class Success(val display: HalachicTimesDisplay?) : Loaded {
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
package il.soulSalttrader.retro.core.event

import android.util.Log
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.core.reducer.Reducible
import il.soulSalttrader.retro.core.reducer.ShabbatDataReducer
import il.soulSalttrader.retro.shabbatApp.model.HalachicTimesDisplay
import il.soulSalttrader.retro.shabbatApp.model.ShabbatDataState

sealed interface ShabbatDataEvent : AppEvent, Reducible<ShabbatDataState> {
    data object Load : ShabbatDataEvent {
        override val reducer = ShabbatDataReducer { ShabbatDataState.Loading }
    }

    sealed interface Loaded : ShabbatDataEvent {
        class Success(val display: HalachicTimesDisplay?) : Loaded {
            override val reducer = ShabbatDataReducer {
                if (Debug.enabled) Log.d("ShabbatEvent.Loaded.Success", "$display")

                ShabbatDataState.Success(display)
            }
        }
        class Failure(val message: String, val cause: Throwable?) : Loaded {
            override val reducer = ShabbatDataReducer {
                if (Debug.enabled) Log.d(
                    "ShabbatEvent.Loaded.Failure",
                    "message: $message, cause: $cause"
                )

                ShabbatDataState.Failure(message, cause)
            }
        }
    }
}
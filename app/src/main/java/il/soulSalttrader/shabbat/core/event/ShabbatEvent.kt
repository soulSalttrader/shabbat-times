package il.soulSalttrader.retro.core.event

import android.util.Log
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.core.Reducible
import il.soulSalttrader.retro.core.ShabbatUiReducer
import il.soulSalttrader.retro.shabbatApp.model.HalachicTimesDisplay
import il.soulSalttrader.retro.shabbatApp.model.ShabbatUiState

sealed interface ShabbatEvent : AppEvent, Reducible<ShabbatUiState> {
    data object Load : ShabbatEvent {
        override val reducer = ShabbatUiReducer { ShabbatUiState.Loading }
    }

    sealed interface Loaded : ShabbatEvent {
        class Success(val display: HalachicTimesDisplay?) : Loaded {
            override val reducer = ShabbatUiReducer {
                if (Debug.enabled) Log.d("ShabbatEvent.Loaded.Success", "$display")

                ShabbatUiState.Success(display)
            }
        }
        class Failure(val message: String, val cause: Throwable?) : Loaded {
            override val reducer = ShabbatUiReducer {
                if (Debug.enabled) Log.d(
                    "ShabbatEvent.Loaded.Failure",
                    "message: $message, cause: $cause"
                )

                ShabbatUiState.Failure(message, cause)
            }
        }
    }
}
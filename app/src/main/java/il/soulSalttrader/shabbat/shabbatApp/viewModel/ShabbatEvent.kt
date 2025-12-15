package il.soulSalttrader.retro.shabbatApp.viewModel

import android.content.Context
import android.util.Log
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.core.Event
import il.soulSalttrader.retro.core.ShabbatUiReducer
import il.soulSalttrader.retro.shabbatApp.model.HalachicTimes
import il.soulSalttrader.retro.shabbatApp.model.ShabbatUiState
import il.soulSalttrader.retro.shabbatApp.model.toDisplay
import il.soulSalttrader.retro.shabbatApp.network.NetworkResult

sealed interface ShabbatEvent : Event<ShabbatUiState> {
    object Load : ShabbatEvent {
        override val reducer = ShabbatUiReducer {
            state -> state
        }
    }

    class Loaded(val result: NetworkResult<HalachicTimes>, context: Context) : ShabbatEvent {
        override val reducer = ShabbatUiReducer {
            state -> when (result) {
                is NetworkResult.Success -> {
                    if (Debug.enabled) Log.d("ShabbatEvent.Loaded", "${result.data}")
                    ShabbatUiState.Success(data = result.data.toDisplay(context))
                }

                is NetworkResult.Failure -> {
                    if (Debug.enabled) Log.d(
                        "ShabbatEvent.Loaded",
                        "message: ${result.message}, cause: ${result.cause}"
                    )
                    ShabbatUiState.Failure(message = result.message, cause = result.cause)
                }
            }
        }
    }
}
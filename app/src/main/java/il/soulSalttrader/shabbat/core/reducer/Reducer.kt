package il.soulSalttrader.retro.core.reducer

import android.util.Log
import il.soulSalttrader.retro.badge.BadgeState
import il.soulSalttrader.retro.breatheApp.BreatheState
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.core.Debug.formatEventName
import il.soulSalttrader.retro.core.Debug.formatStateTransition
import il.soulSalttrader.retro.core.model.State
import il.soulSalttrader.retro.counterApp.CounterState
import il.soulSalttrader.retro.shabbatApp.model.ShabbatUiState
import il.soulSalttrader.retro.timerApp.TimerState

fun interface Reducer<S : State> {
    infix fun reduce(state: S): S

    infix fun then(next: Reducer<S>) = Reducer<S> { next.reduce(reduce(it)) }

    infix fun debug(tag: String) = takeIf { Debug.enabled }
        ?.let { baseReducer ->
            Reducer { state ->
                val nextState = reduce(state)
                val eventName = baseReducer.formatEventName()

                Log.d(tag,state.formatStateTransition(eventName = eventName, next = nextState))
                nextState
            }
        } ?: this
}

typealias BreatheReducer = Reducer<BreatheState>
typealias CounterReducer = Reducer<CounterState>
typealias TimerReducer = Reducer<TimerState>
typealias BadgeReducer = Reducer<BadgeState>
typealias ShabbatUiReducer = Reducer<ShabbatUiState>
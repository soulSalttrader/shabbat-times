package il.soulSalttrader.shabbattimes.reducer

import android.util.Log
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.Debug.formatEventName
import il.soulSalttrader.shabbattimes.Debug.formatStateTransition
import il.soulSalttrader.shabbattimes.badge.BadgeState
import il.soulSalttrader.shabbattimes.breatheApp.BreatheState
import il.soulSalttrader.shabbattimes.model.State
import il.soulSalttrader.shabbattimes.counterApp.CounterState
import il.soulSalttrader.shabbattimes.shabbatApp.model.ShabbatState
import il.soulSalttrader.shabbattimes.timerApp.TimerState

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
typealias ShabbatReducer = Reducer<ShabbatState>
package il.soulSalttrader.shabbattimes.reducer

import android.util.Log
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.Debug.formatEventName
import il.soulSalttrader.shabbattimes.Debug.formatStateTransition
import il.soulSalttrader.shabbattimes.content.search.SearchUiState
import il.soulSalttrader.shabbattimes.content.shabbat.ShabbatUiState
import il.soulSalttrader.shabbattimes.location.LocationUiState
import il.soulSalttrader.shabbattimes.model.State

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

typealias ShabbatReducer = Reducer<ShabbatUiState>
typealias SearchReducer = Reducer<SearchUiState>
typealias LocationReducer = Reducer<LocationUiState>
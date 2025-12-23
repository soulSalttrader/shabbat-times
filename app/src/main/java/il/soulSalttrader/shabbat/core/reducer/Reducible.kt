package il.soulSalttrader.retro.core.reducer

import il.soulSalttrader.retro.core.model.State

interface Reducible<S : State> {
    val reducer: Reducer<S>
}
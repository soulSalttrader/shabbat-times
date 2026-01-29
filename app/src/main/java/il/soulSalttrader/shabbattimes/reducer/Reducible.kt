package il.soulSalttrader.shabbattimes.reducer

import il.soulSalttrader.shabbattimes.model.State

interface Reducible<S : State> {
    val reducer: Reducer<S>
}
package il.soulSalttrader.shabbattimes.ui.reducer

import il.soulSalttrader.shabbattimes.model.State

interface Reducible<S : State> {
    val reducer: Reducer<S>
}
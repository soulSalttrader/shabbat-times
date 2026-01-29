package il.soulSalttrader.shabbattimes.nav.graph

import androidx.navigation.NavGraphBuilder
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.model.State

fun NavGraphBuilder.mainNavGraph(
    state: State,
    onEvent: (AppEvent) -> Unit,
) {
    topNavGraph(state = state, onEvent = onEvent)
    bottomNavGraph(state = state, onEvent = onEvent)
}
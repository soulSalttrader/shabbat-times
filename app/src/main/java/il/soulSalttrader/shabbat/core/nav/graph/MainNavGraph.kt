package il.soulSalttrader.retro.core.nav.graph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import il.soulSalttrader.retro.core.BadgeReducer
import il.soulSalttrader.retro.core.Model
import il.soulSalttrader.retro.core.Reduce
import il.soulSalttrader.retro.core.content.SplitScreen
import il.soulSalttrader.retro.core.nav.NavManager
import il.soulSalttrader.retro.counterApp.CounterScreen
import il.soulSalttrader.retro.counterApp.remember.CounterRememberScreen
import il.soulSalttrader.retro.navigation.experimental.CounterScreen

fun NavGraphBuilder.mainNavGraph(
    modifier: Modifier,
    navManager: NavManager,
    state: Model,
    reducers: Reduce,
    onBadgeReducer: (BadgeReducer) -> Unit
) {
    topNavGraph(onBadgeReducer)
    bottomNavGraph(modifier, navManager, state, reducers)

    composable<CounterScreen> {
        SplitScreen(
            modifier = Modifier.fillMaxSize(),
            topContent = {
                CounterScreen(
                    state = state.counterState,
                    onReduce = reducers.onCounterReducer,
                )
            },
            bottomContent = {
                CounterRememberScreen()
            }
        )
    }
}
package il.soulSalttrader.retro.core.nav.graph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import il.soulSalttrader.retro.core.AppModel
import il.soulSalttrader.retro.core.content.SplitScreen
import il.soulSalttrader.retro.core.event.AppEvent
import il.soulSalttrader.retro.counterApp.CounterScreen
import il.soulSalttrader.retro.counterApp.remember.CounterRememberScreen
import il.soulSalttrader.retro.navigation.experimental.CounterScreen

fun NavGraphBuilder.mainNavGraph(
    state: AppModel,
    onEvent: (AppEvent) -> Unit,
) {
    topNavGraph(onEvent = onEvent)
    bottomNavGraph(state = state, onEvent = onEvent)

    composable<CounterScreen> {
        SplitScreen(
            modifier = Modifier.fillMaxSize(),
            topContent = {
                CounterScreen(
                    state = state.counter,
                    onEvent = onEvent,
                )
            },
            bottomContent = {
                CounterRememberScreen()
            }
        )
    }
}
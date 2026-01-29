package il.soulSalttrader.shabbattimes.nav.graph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import il.soulSalttrader.shabbattimes.core.model.AppModel
import il.soulSalttrader.shabbattimes.core.content.SplitScreen
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.counterApp.CounterScreen
import il.soulSalttrader.shabbattimes.counterApp.remember.CounterRememberScreen
import il.soulSalttrader.shabbattimes.navigation.experimental.CounterScreen

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
package il.soulSalttrader.shabbattimes.nav.graph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import il.soulSalttrader.shabbattimes.breatheApp.BreatheScreen
import il.soulSalttrader.shabbattimes.core.model.AppModel
import il.soulSalttrader.shabbattimes.core.content.SplitScreen
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.nav.NavTargetBottom
import il.soulSalttrader.shabbattimes.counterApp.CounterScreen
import il.soulSalttrader.shabbattimes.counterApp.remember.CounterRememberScreen
import il.soulSalttrader.shabbattimes.nav.NavItems
import il.soulSalttrader.shabbattimes.shabbatApp.content.FailureScreen
import il.soulSalttrader.shabbattimes.shabbatApp.content.ShabbatScreen
import il.soulSalttrader.shabbattimes.timerApp.TimerScreen

fun NavGraphBuilder.bottomNavGraph(
    state: AppModel,
    onEvent: (AppEvent) -> Unit,
) {
    composable<NavTargetBottom.Alerts> { FailureScreen(message = "No internet connection") { } }

    composable<NavTargetBottom.Breathe> {
        NavItems.Breathe.title?.let { text -> Text(text = text) }

        BreatheScreen(
            modifier = Modifier,
            state = state,
            onEvent = onEvent,
        )
    }

    composable<NavTargetBottom.Home> {
        NavItems.Home.title?.let { text -> Text(text = text) }

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
    composable<NavTargetBottom.Settings> {
        NavItems.Settings.title?.let { text -> Text(text = text) }

        TimerScreen(
            state = state.timer,
            onEvent = onEvent,
        )
    }

    composable<NavTargetBottom.Shabbat> { ShabbatScreen() }
}
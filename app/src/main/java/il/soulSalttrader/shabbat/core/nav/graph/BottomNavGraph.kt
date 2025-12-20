package il.soulSalttrader.retro.core.nav.graph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import il.soulSalttrader.retro.breatheApp.BreatheScreen
import il.soulSalttrader.retro.core.AppModel
import il.soulSalttrader.retro.core.content.SplitScreen
import il.soulSalttrader.retro.core.event.AppEvent
import il.soulSalttrader.retro.core.nav.NavItems.Breathe
import il.soulSalttrader.retro.core.nav.NavItems.Home
import il.soulSalttrader.retro.core.nav.NavItems.Settings
import il.soulSalttrader.retro.core.nav.NavTargetBottom
import il.soulSalttrader.retro.counterApp.CounterScreen
import il.soulSalttrader.retro.counterApp.remember.CounterRememberScreen
import il.soulSalttrader.retro.shabbatApp.content.FailureScreen
import il.soulSalttrader.retro.shabbatApp.content.ShabbatScreen
import il.soulSalttrader.retro.timerApp.TimerScreen

fun NavGraphBuilder.bottomNavGraph(
    state: AppModel,
    onEvent: (AppEvent) -> Unit,
) {
    composable<NavTargetBottom.Alerts> { FailureScreen(message = "No internet connection") { } }

    composable<NavTargetBottom.Breathe> {
        Breathe.title?.let { text -> Text(text = text) }

        BreatheScreen(
            modifier = Modifier,
            state = state,
            onEvent = onEvent,
        )
    }

    composable<NavTargetBottom.Home> {
        Home.title?.let { text -> Text(text = text) }

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
        Settings.title?.let { text -> Text(text = text) }

        TimerScreen(
            state = state.timer,
            onEvent = onEvent,
        )
    }

    composable<NavTargetBottom.Shabbat> { ShabbatScreen() }
}
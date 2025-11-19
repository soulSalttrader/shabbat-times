package il.soulSalttrader.retro.core.nav.graph

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import il.soulSalttrader.retro.breatheApp.BreatheScreen
import il.soulSalttrader.retro.core.Model
import il.soulSalttrader.retro.core.Reduce
import il.soulSalttrader.retro.core.content.SplitScreen
import il.soulSalttrader.retro.core.nav.NavItems.Breathe
import il.soulSalttrader.retro.core.nav.NavItems.Home
import il.soulSalttrader.retro.core.nav.NavItems.Settings
import il.soulSalttrader.retro.core.nav.NavItems.Shabbat
import il.soulSalttrader.retro.core.nav.NavTargetBottom
import il.soulSalttrader.retro.core.simpleName
import il.soulSalttrader.retro.counterApp.CounterScreen
import il.soulSalttrader.retro.counterApp.remember.CounterRememberScreen
import il.soulSalttrader.retro.timerApp.TimerScreen

fun NavGraphBuilder.bottomNavGraph(
    modifier: Modifier,
    navController: NavHostController,
    state: Model,
    reducers: Reduce,
) {
    composable<NavTargetBottom.Alerts> {
        val args = it.toRoute<NavTargetBottom.Alerts>()
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = { navController.popBackStack() }) {
                Text(text = "title: ${args.simpleName()}.")
            }
        }
    }

    composable<NavTargetBottom.Breathe> {
        Breathe.title?.let { text -> Text(text = text) }

        BreatheScreen(
            modifier = modifier,
            breatheState = state.breatheState,
            timerState = state.timerState,
            onBreatheReduce = reducers.onBreatheReducer,
            onTimerReduce = reducers.onTimerReducer,
        )
    }

    composable<NavTargetBottom.Home> {
        Home.title?.let { text -> Text(text = text) }

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
    composable<NavTargetBottom.Settings> {
        Settings.title?.let { text -> Text(text = text) }

        TimerScreen(
            state = state.timerState,
            onReduce = reducers.onTimerReducer,
        )
    }

    composable<NavTargetBottom.Shabbat> { Shabbat.title?.let { text -> Text(text = text) } }
}
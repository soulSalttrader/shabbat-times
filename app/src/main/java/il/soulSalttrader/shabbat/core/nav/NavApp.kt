package il.soulSalttrader.retro.core.nav

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import il.soulSalttrader.retro.badge.BadgeButton
import il.soulSalttrader.retro.badge.BadgeButtons
import il.soulSalttrader.retro.breatheApp.BreatheScreen
import il.soulSalttrader.retro.core.BadgeReducer
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.core.Model
import il.soulSalttrader.retro.core.Reduce
import il.soulSalttrader.retro.core.content.HistoryScreen
import il.soulSalttrader.retro.core.content.SplitScreen
import il.soulSalttrader.retro.core.nav.NavItems.Alerts
import il.soulSalttrader.retro.core.nav.NavItems.Breathe
import il.soulSalttrader.retro.core.nav.NavItems.History
import il.soulSalttrader.retro.core.nav.NavItems.Home
import il.soulSalttrader.retro.core.nav.NavItems.Settings
import il.soulSalttrader.retro.core.nav.NavItems.Shabbat
import il.soulSalttrader.retro.core.nav.NavTarget.Companion.fromBackStackEntry
import il.soulSalttrader.retro.core.simpleName
import il.soulSalttrader.retro.counterApp.CounterScreen
import il.soulSalttrader.retro.counterApp.remember.CounterRememberScreen
import il.soulSalttrader.retro.navigation.experimental.CounterScreen
import il.soulSalttrader.retro.timerApp.TimerScreen

@Composable
fun NavApp(
    modifier: Modifier,
    state: Model,
    reducers: Reduce,
    onBadgeReducer: (BadgeReducer) -> Unit,
    navManager: NavManager,
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(currentBackStackEntry) {
        val target = currentBackStackEntry.fromBackStackEntry()
        if (Debug.enabled) {
            val route = currentBackStackEntry?.destination?.route
            Log.d("updated target", "T:$target\nR:$route")
        }
        navManager.updateCurrentTarget(target)
    }

    LaunchedEffect(Unit) {
        navManager.commands.collect { action ->
            when (action) {
                is NavAction.To        -> navController.navigate(action.target) {
                    launchSingleTop = true
                    restoreState = true
                }
                is NavAction.Up        -> navController.popBackStack()
                is NavAction.ResetTo   -> navController.navigate(action.target) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
                is NavAction.PopTo     -> navController.popBackStack(action.target, inclusive = false)
                is NavAction.PopToRoot -> navController.popBackStack(navController.graph.startDestinationId, inclusive = false)
            }
        }
    }

    val startDestination = NavTargetBottom.Home

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<NavTargetBottom.Alerts> {
            val args = it.toRoute<NavTargetBottom.Alerts>()
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = { navController.popBackStack() }) { Text(text = "title: ${args.simpleName()}.") }
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

        composable<NavTargetTop.History> {
            HistoryScreen()
        }

        composable<NavTargetTop.Favorite> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Badges",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineMedium
                )

                BadgeButtons(
                    rightButtons = listOf(
                        BadgeButton.increment(navItems = listOf(Alerts, Breathe, History, Home)),
                        BadgeButton.decrement(navItems = listOf(Alerts, Breathe, History, Home)),
                    ),
                    leftButtons = listOf(
                        BadgeButton.clear(navItems = listOf(Alerts, Home)),
                        BadgeButton.clearAll()
                    ),
                    onReduce = onBadgeReducer
                )
            }
        }
    }

    if (Debug.enabled) {
        LaunchedEffect(navController) {
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                Log.d("navController", "Current destination: ${navController.currentDestinationName()}")
            }
        }
    }
}
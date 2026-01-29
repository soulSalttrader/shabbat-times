package il.soulSalttrader.shabbattimes.nav

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.nav.common.collectNavigationCommands
import il.soulSalttrader.shabbattimes.nav.common.currentDestinationName
import il.soulSalttrader.shabbattimes.nav.common.syncBackStackWithNavigator
import il.soulSalttrader.shabbattimes.nav.graph.mainNavGraph
import il.soulSalttrader.shabbattimes.model.State


@Composable
fun NavApp(
    modifier: Modifier,
    state: State,
    onEvent: (AppEvent) -> Unit,
    navigator: Navigator,
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(currentBackStackEntry) {
        navigator.syncBackStackWithNavigator(currentBackStackEntry)
    }

    LaunchedEffect(Unit) {
        navigator.collectNavigationCommands(navController)
    }

    val startDestination = NavTargetBottom.Shabbat

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        mainNavGraph(
            state = state,
            onEvent = onEvent,
        )
    }

    if (Debug.enabled) {
        LaunchedEffect(navController) {
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                Log.d("navController", "Current destination: ${navController.currentDestinationName()}")
            }
        }
    }
}
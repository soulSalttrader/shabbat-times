package il.soulSalttrader.retro.core.nav

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import il.soulSalttrader.retro.core.AppModel
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.core.EventHandler
import il.soulSalttrader.retro.core.nav.graph.mainNavGraph


@Composable
fun NavApp(
    modifier: Modifier,
    state: AppModel,
    eventHandler: EventHandler,
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

    val startDestination = NavTargetBottom.Home

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        mainNavGraph(
            state = state,
            eventHandler = eventHandler,
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
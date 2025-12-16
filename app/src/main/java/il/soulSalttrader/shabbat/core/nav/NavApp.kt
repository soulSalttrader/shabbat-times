package il.soulSalttrader.retro.core.nav

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import il.soulSalttrader.retro.core.BadgeReducer
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.core.Model
import il.soulSalttrader.retro.core.Reduce
import il.soulSalttrader.retro.core.nav.graph.mainNavGraph

@Composable
fun NavApp(
    modifier: Modifier,
    state: Model,
    reducers: Reduce,
    onBadgeReducer: (BadgeReducer) -> Unit,
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
            modifier = modifier,
            navigator = navigator,
            state = state,
            reducers = reducers,
            onBadgeReducer = onBadgeReducer,
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
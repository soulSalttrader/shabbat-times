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
import il.soulSalttrader.retro.core.nav.NavTarget.Companion.fromBackStackEntry
import il.soulSalttrader.retro.core.nav.graph.mainNavGraph

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
                is NavAction.To        -> navController.navigate(action.target)
                is NavAction.Up        -> navController.popBackStack()
                is NavAction.ResetTo   -> navController.navigate(action.target)
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
        mainNavGraph(
            modifier = modifier,
            navManager = navManager,
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
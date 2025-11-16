package il.soulSalttrader.retro.core.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun NavApp(navManager: NavManager) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        navManager.commands.collect { action ->
            when (action) {
                is NavAction.To        -> navController.navigate(action.target)
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
        navController = navController,
        startDestination = startDestination,
    ) { /* TODO() */}
}
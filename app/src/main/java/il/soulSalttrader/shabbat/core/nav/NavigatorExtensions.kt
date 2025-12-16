package il.soulSalttrader.retro.core.nav

import android.util.Log
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.core.nav.NavTarget.Companion.fromBackStackEntry

fun Navigator.syncBackStackWithNavigator(currentBackStackEntry: NavBackStackEntry?) {
    val target = currentBackStackEntry.fromBackStackEntry()
    if (Debug.enabled) {
        val route = currentBackStackEntry?.destination?.route
        Log.d("updated target", "T:$target\nR:$route")
    }
    this.updateCurrentTarget(target)
}

suspend fun Navigator.collectNavigationCommands(navController: NavHostController): Nothing {
    this.commands.collect { action ->
        when (action) {
            is NavAction.To        -> navController.navigate(action.target)
            is NavAction.Up        -> navController.popBackStack()
            is NavAction.ResetTo   -> navController.navigate(action.target)
            is NavAction.PopTo     -> navController.popBackStack(action.target, inclusive = false)
            is NavAction.PopToRoot -> navController.popBackStack(
                navController.graph.startDestinationId,
                inclusive = false
            )
        }
    }
}
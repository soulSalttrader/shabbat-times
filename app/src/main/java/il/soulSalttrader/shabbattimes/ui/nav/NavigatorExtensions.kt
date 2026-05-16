package il.soulSalttrader.shabbattimes.ui.nav

import android.util.Log
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.ui.nav.NavTarget.Companion.fromBackStackEntry

fun Navigator.syncBackStackWithNavigator(currentBackStackEntry: NavBackStackEntry?) {
    val target = currentBackStackEntry.fromBackStackEntry()
    if (Debug.enabled) {
        val route = currentBackStackEntry?.destination?.route
        Log.d("updated target", "T:$target\nR:$route")
    }
    this.updateCurrentTarget(target)
}

suspend fun Navigator.collectNavigationCommands(navController: NavHostController): Nothing {
    commands.collect { action ->
        with(navController) {
            when (action) {
                is NavAction.To        -> handleTo(action)
                is NavAction.Up        -> handleUp()
                is NavAction.ResetTo   -> handleResetTo(action)
                is NavAction.PopTo     -> handlePopTo(action)
                is NavAction.PopToRoot -> handlePopToRoot()
            }
        }
    }
}
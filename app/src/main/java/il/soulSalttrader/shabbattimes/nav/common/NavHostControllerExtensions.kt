package il.soulSalttrader.shabbattimes.nav.common

import android.util.Log
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.nav.NavAction

fun NavHostController.handleTo(action: NavAction.To) {
    val targetRoute = action.target.route()
    val wasAlreadyThere = currentDestination?.route == targetRoute

    navigate(action.target) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }

    if (Debug.enabled && wasAlreadyThere) {
        Log.d(
            "Navigator",
            "Self-navigation ignored → already at $targetRoute"
        )
    }
}

fun NavHostController.handleUp() {
    previousBackStackEntry?.let {
        navigateUp()
    } ?: run {
        if (Debug.enabled) Log.d(
            "Navigator",
            "Up ignored → already at root"
        )
    }
}

fun NavHostController.handleResetTo(action: NavAction.ResetTo) {
    if (Debug.enabled) {
        val targetRoute = action.target.route()
        Log.d(
            "Navigator",
            "ResetTo -> clearing stack and navigating to $targetRoute"
        )
    }

    navigate(action.target) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

fun NavHostController.handlePopTo(action: NavAction.PopTo) {
    if (Debug.enabled) Log.d(
        "Navigator",
        "PopTo target not found in back stack: ${action.target}"
    )

    popBackStack(action.target, inclusive = false)
}

fun NavHostController.handlePopToRoot() {
    if (Debug.enabled) {
        val rootId = graph.startDestinationId
        val rootRoute = graph.findNode(rootId)?.route ?: "Unknown"
        Log.d("Navigator", "PopToRoot → returning to root ($rootRoute)")
    }

    popBackStack(
        graph.startDestinationId,
        inclusive = false
    )
}

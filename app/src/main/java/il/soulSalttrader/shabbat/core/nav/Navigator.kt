package il.soulSalttrader.retro.core.nav

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface Navigator {
    val commands: SharedFlow<NavAction>
    val currentTarget: StateFlow<NavTarget?>

    fun navigateTo(
        target: NavTarget,
        navOptions: NavOptionsBuilder.() -> Unit = {
            launchSingleTop = true
            restoreState = true
        },
    ): Boolean

    fun resetRoot(
        target: NavTarget,
        navOptions: NavOptionsBuilder.() -> Unit = {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        },
    ): Boolean

    fun popTo(
        target: NavTarget,
        navOptions: NavOptionsBuilder.() -> Unit = { },
    ): Boolean

    fun navigateUp(): Boolean
    fun popToRoot(): Boolean

    fun updateCurrentTarget(target: NavTarget?)
}
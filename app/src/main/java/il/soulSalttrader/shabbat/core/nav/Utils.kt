package il.soulSalttrader.retro.core.nav

import androidx.navigation.NavController
import androidx.navigation.NavDestination

inline fun <reified T: Any> allSealedObjects(): List<T> =
    T::class.sealedSubclasses.mapNotNull { it.objectInstance }

fun NavController.currentDestinationName(): String? =
    currentBackStackEntry?.destination?.route?.substringAfterLast('.')

fun NavDestination?.currentDestinationName(): String? =
    this?.route?.substringAfterLast('.')

fun List<NavItem>.extractTopBarItems(): Pair<NavItem?, List<NavItem>> {
    val topNavigationItem = this.find { it.role == NavRole.TOP_NAVIGATION }
    val topActionItems = this.filter { it.role == NavRole.TOP_ACTION }
    return topNavigationItem to topActionItems
}

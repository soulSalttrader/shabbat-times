package il.soulSalttrader.retro.core.nav

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import il.soulSalttrader.retro.core.nav.destination.Destination
import il.soulSalttrader.retro.core.nav.destination.DestinationNavBottom
import il.soulSalttrader.retro.core.nav.destination.DestinationNavTop

inline fun <reified T: Any> allSealedObjects(): List<T> =
    T::class.sealedSubclasses.mapNotNull { it.objectInstance }

val allDestinationBottoms by lazy { allSealedObjects<DestinationNavBottom>() }
val allDestinationTops by lazy { allSealedObjects<DestinationNavTop>() }

fun NavController.currentDestinationName(): String? =
    currentBackStackEntry?.destination?.route?.substringAfterLast('.')

fun NavDestination?.currentDestinationName(): String? =
    this?.route?.substringAfterLast('.')

fun List<Destination>.extractToBarItems(): Pair<Destination?, List<Destination>> {
    val navItem = this.find { it.role == NavRole.TOP_NAVIGATION }
    val actionItems = this.filter { it.role == NavRole.TOP_ACTION }
    return navItem to actionItems
}

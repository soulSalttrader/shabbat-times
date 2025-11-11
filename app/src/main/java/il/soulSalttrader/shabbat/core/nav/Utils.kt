package il.soulSalttrader.retro.core.nav

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import il.soulSalttrader.retro.core.nav.destination.DestinationNavBottom
import il.soulSalttrader.retro.core.nav.destination.DestinationNavTop
import il.soulSalttrader.retro.core.nav.destination.DestinationTop
import il.soulSalttrader.retro.core.nav.destination.TopBarRole

inline fun <reified T: Any> allSealedObjects(): List<T> =
    T::class.sealedSubclasses.mapNotNull { it.objectInstance }

val allDestinationBottoms by lazy { allSealedObjects<DestinationNavBottom>() }
val allDestinationTops by lazy { allSealedObjects<DestinationNavTop>() }

fun NavController.currentDestinationName(): String? =
    currentBackStackEntry?.destination?.route?.substringAfterLast('.')

fun NavDestination?.currentDestinationName(): String? =
    this?.route?.substringAfterLast('.')

fun List<DestinationTop>.extractToBarItems(): Pair<DestinationTop?, List<DestinationTop>> {
    val navItem = this.find { it.role == TopBarRole.NAVIGATION }
    val actionItems = this.filter { it.role == TopBarRole.ACTION }
    return navItem to actionItems
}

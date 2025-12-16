package il.soulSalttrader.retro.core.nav

import androidx.navigation.NavDestination

fun NavDestination?.currentDestinationName(): String? =
    this?.route?.substringAfterLast('.')
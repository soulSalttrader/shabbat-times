package il.soulSalttrader.retro.core.nav.common

import androidx.navigation.NavDestination

fun NavDestination?.currentDestinationName(): String? =
    this?.route?.substringAfterLast('.')
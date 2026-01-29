package il.soulSalttrader.shabbattimes.nav.common

import androidx.navigation.NavDestination

fun NavDestination?.currentDestinationName(): String? =
    this?.route?.substringAfterLast('.')
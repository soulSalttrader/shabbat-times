package il.soulSalttrader.shabbattimes.ui.nav

import androidx.navigation.NavDestination

fun NavDestination?.currentDestinationName(): String? =
    this?.route?.substringAfterLast('.')
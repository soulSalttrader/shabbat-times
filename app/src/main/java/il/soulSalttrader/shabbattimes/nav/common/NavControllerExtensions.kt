package il.soulSalttrader.shabbattimes.nav.common

import androidx.navigation.NavController

fun NavController.currentDestinationName(): String? =
    currentBackStackEntry?.destination?.route?.substringAfterLast('.')
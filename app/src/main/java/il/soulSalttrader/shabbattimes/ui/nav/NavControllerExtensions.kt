package il.soulSalttrader.shabbattimes.ui.nav

import androidx.navigation.NavController

fun NavController.currentDestinationName(): String? =
    currentBackStackEntry?.destination?.route?.substringAfterLast('.')
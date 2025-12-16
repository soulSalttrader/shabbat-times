package il.soulSalttrader.retro.core.nav

import androidx.navigation.NavController

fun NavController.currentDestinationName(): String? =
    currentBackStackEntry?.destination?.route?.substringAfterLast('.')
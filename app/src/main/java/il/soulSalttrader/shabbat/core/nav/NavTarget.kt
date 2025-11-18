package il.soulSalttrader.retro.core.nav

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavTarget {
    companion object {
        fun NavBackStackEntry?.fromBackStackEntry(): NavTarget? {
            return when {
                this?.destination?.hasRoute<NavTargetBottom.Alerts>() == true   -> NavTargetBottom.Alerts
                this?.destination?.hasRoute<NavTargetBottom.Breathe>() == true  -> NavTargetBottom.Breathe
                this?.destination?.hasRoute<NavTargetBottom.Home>() == true     -> NavTargetBottom.Home
                this?.destination?.hasRoute<NavTargetBottom.Settings>() == true -> NavTargetBottom.Settings
                this?.destination?.hasRoute<NavTargetBottom.Shabbat>() == true  -> NavTargetBottom.Shabbat

                this?.destination?.hasRoute<NavTargetTop.Previous>() == true    -> NavTargetTop.Previous
                this?.destination?.hasRoute<NavTargetTop.Favorite>() == true    -> NavTargetTop.Favorite
                this?.destination?.hasRoute<NavTargetTop.History>() == true     -> NavTargetTop.History
                else                                                            -> null
            }
        }
     }
}
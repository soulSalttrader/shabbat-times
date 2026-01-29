package il.soulSalttrader.shabbattimes.nav

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavTargetBottom : NavTarget {
    @Serializable object Home : NavTargetBottom
    @Serializable object Alerts : NavTargetBottom
    @Serializable object Settings : NavTargetBottom
    @Serializable object More : NavTargetBottom
    @Serializable object Breathe : NavTargetBottom
    @Serializable object Shabbat : NavTargetBottom
}
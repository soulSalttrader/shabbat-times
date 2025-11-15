package il.soulSalttrader.retro.core.nav.route

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavTargetBottom : NavTarget {
    @Serializable object HomeScreen : NavTargetBottom
    @Serializable object AlertsScreen : NavTargetBottom
    @Serializable object SettingsScreen : NavTargetBottom
//    @Serializable object MoreScreen : NavTargetBottom
    @Serializable object BreatheScreen : NavTargetBottom
    @Serializable object ShabbatScreen : NavTargetBottom
}
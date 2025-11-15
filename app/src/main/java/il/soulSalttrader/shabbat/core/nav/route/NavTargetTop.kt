package il.soulSalttrader.retro.core.nav.route

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavTargetTop : NavTarget {
    @Serializable object PreviousScreen : NavTargetTop
    @Serializable object HistoryScreen : NavTargetTop
    @Serializable object FavoriteScreen : NavTargetTop
}
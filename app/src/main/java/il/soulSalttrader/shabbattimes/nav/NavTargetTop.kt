package il.soulSalttrader.shabbattimes.nav

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavTargetTop : NavTarget {
    @Serializable object Previous : NavTargetTop
    @Serializable object History : NavTargetTop
    @Serializable object Favorite : NavTargetTop
}
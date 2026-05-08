package il.soulSalttrader.shabbattimes.ui.nav

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavTargetTop : NavTarget {
    @Serializable object Previous : NavTargetTop
    @Serializable object Settings : NavTargetBottom
}
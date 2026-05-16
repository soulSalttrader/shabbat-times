package il.soulSalttrader.shabbattimes.ui.nav

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavTargetBottom : NavTarget {
    @Serializable object Shabbat : NavTargetBottom
}
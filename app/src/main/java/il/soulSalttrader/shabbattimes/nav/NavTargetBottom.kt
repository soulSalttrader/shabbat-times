package il.soulSalttrader.shabbattimes.nav

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavTargetBottom : NavTarget {
    @Serializable object Shabbat : NavTargetBottom
}
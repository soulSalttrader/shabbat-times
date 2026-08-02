package il.soulSalttrader.shabbattimes.ui.nav

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavTargetRoot : NavTarget {
    @Serializable object Shabbat : NavTargetRoot
}

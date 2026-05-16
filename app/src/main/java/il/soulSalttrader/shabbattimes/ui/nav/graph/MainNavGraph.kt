package il.soulSalttrader.shabbattimes.ui.nav.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import il.soulSalttrader.shabbattimes.ui.nav.NavTargetBottom
import il.soulSalttrader.shabbattimes.ui.nav.NavTargetTop
import il.soulSalttrader.shabbattimes.ui.settings.SettingsScreen
import il.soulSalttrader.shabbattimes.ui.shabbat.ShabbatScreen

fun NavGraphBuilder.mainNavGraph() {
    composable<NavTargetBottom.Shabbat> { ShabbatScreen() }
    composable<NavTargetTop.Settings> { SettingsScreen() }
}
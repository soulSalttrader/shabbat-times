package il.soulSalttrader.shabbattimes.ui.nav.graph

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import il.soulSalttrader.shabbattimes.ui.nav.NavTargetBottom
import il.soulSalttrader.shabbattimes.ui.nav.NavTargetTop
import il.soulSalttrader.shabbattimes.ui.settings.SettingsScreen
import il.soulSalttrader.shabbattimes.ui.shabbat.ShabbatScreen

fun NavGraphBuilder.mainNavGraph(snackbarHostState: SnackbarHostState) {
    composable<NavTargetBottom.Shabbat> { ShabbatScreen(snackbarHostState) }
    composable<NavTargetTop.Settings> { SettingsScreen() }
}
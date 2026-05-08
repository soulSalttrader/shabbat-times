package il.soulSalttrader.shabbattimes.ui.nav.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import il.soulSalttrader.shabbattimes.ui.FailureScreen
import il.soulSalttrader.shabbattimes.ui.shabbat.ShabbatScreen
import il.soulSalttrader.shabbattimes.ui.nav.NavTargetBottom
import il.soulSalttrader.shabbattimes.ui.nav.NavTargetTop
import il.soulSalttrader.shabbattimes.ui.nav.Navigator

fun NavGraphBuilder.mainNavGraph(navigator: Navigator) {
    composable<NavTargetBottom.Shabbat> {
        ShabbatScreen()
    }

    composable<NavTargetTop.Settings> {
        FailureScreen(message = "Coming Soon") {
            navigator.navigateUp()
        }
    }
}
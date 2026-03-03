package il.soulSalttrader.shabbattimes.nav.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import il.soulSalttrader.shabbattimes.content.FailureScreen
import il.soulSalttrader.shabbattimes.content.shabbat.ShabbatScreen
import il.soulSalttrader.shabbattimes.nav.NavTargetBottom
import il.soulSalttrader.shabbattimes.nav.NavTargetTop
import il.soulSalttrader.shabbattimes.nav.Navigator

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
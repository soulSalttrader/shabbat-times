package il.soulSalttrader.shabbattimes.nav.graph

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import il.soulSalttrader.shabbattimes.content.FailureScreen
import il.soulSalttrader.shabbattimes.content.ShabbatScreen
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.nav.NavItems
import il.soulSalttrader.shabbattimes.nav.NavTargetBottom
import il.soulSalttrader.shabbattimes.model.State

fun NavGraphBuilder.bottomNavGraph(
    state: State,
    onEvent: (AppEvent) -> Unit,
) {
    composable<NavTargetBottom.Alerts> {
        FailureScreen(message = "No internet connection") { }
    }

    composable<NavTargetBottom.Breathe> {
        NavItems.Breathe.title?.let { text -> Text(text = text) }
    }

    composable<NavTargetBottom.Home> {
        NavItems.Home.title?.let { text -> Text(text = text) }
    }
    composable<NavTargetBottom.Settings> {
        NavItems.Settings.title?.let { text -> Text(text = text) }
    }

    composable<NavTargetBottom.Shabbat> { ShabbatScreen() }
}
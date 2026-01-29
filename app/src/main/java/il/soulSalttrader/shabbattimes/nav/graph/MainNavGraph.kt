package il.soulSalttrader.shabbattimes.nav.graph

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import il.soulSalttrader.shabbattimes.content.ShabbatScreen
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.model.State
import il.soulSalttrader.shabbattimes.nav.NavItems
import il.soulSalttrader.shabbattimes.nav.NavTargetBottom
import il.soulSalttrader.shabbattimes.nav.NavTargetTop

fun NavGraphBuilder.mainNavGraph(
    state: State,
    onEvent: (AppEvent) -> Unit,
) {
    composable<NavTargetBottom.Shabbat> {
        ShabbatScreen()
    }

    composable<NavTargetTop.Settings> {
        NavItems.Settings.title?.let { text -> Text(text = text) }
    }

    composable<NavTargetTop.Previous> {
        NavItems.Previous.title?.let { text -> Text(text = text) }
    }
}
package il.soulSalttrader.shabbattimes.nav.graph

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.nav.NavItems
import il.soulSalttrader.shabbattimes.nav.NavTargetTop
import il.soulSalttrader.shabbattimes.model.State

fun NavGraphBuilder.topNavGraph(
    state: State,
    onEvent: (AppEvent) -> Unit,
) {
    composable<NavTargetTop.History> {
        NavItems.History.title?.let { text -> Text(text = text) }
    }

    composable<NavTargetTop.Favorite> {
        NavItems.Settings.title?.let { text -> Text(text = text) }
    }
}


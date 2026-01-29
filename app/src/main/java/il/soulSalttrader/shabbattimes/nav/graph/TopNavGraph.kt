package il.soulSalttrader.shabbattimes.nav.graph

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import il.soulSalttrader.shabbattimes.badge.BadgeButton
import il.soulSalttrader.shabbattimes.badge.BadgeButtons
import il.soulSalttrader.shabbattimes.core.content.HistoryScreen
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.nav.NavTargetTop
import il.soulSalttrader.shabbattimes.nav.NavItems

fun NavGraphBuilder.topNavGraph(
    onEvent: (AppEvent) -> Unit,
) {
    composable<NavTargetTop.History> {
        HistoryScreen()
    }

    composable<NavTargetTop.Favorite> {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Badges",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineMedium
            )

            BadgeButtons(
                rightButtons = listOf(
                    BadgeButton.increment(navItems = listOf(
                        NavItems.Alerts,
                        NavItems.Breathe,
                        NavItems.History,
                        NavItems.Home
                    )),
                    BadgeButton.decrement(navItems = listOf(
                        NavItems.Alerts,
                        NavItems.Breathe,
                        NavItems.History,
                        NavItems.Home
                    )),
                ),
                leftButtons = listOf(
                    BadgeButton.clear(navItems = listOf(NavItems.Alerts, NavItems.Home)),
                    BadgeButton.clearAll()
                ),
                onEvent = onEvent,
            )
        }
    }
}


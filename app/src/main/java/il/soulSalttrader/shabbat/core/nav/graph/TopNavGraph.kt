package il.soulSalttrader.retro.core.nav.graph

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import il.soulSalttrader.retro.badge.BadgeButton
import il.soulSalttrader.retro.badge.BadgeButtons
import il.soulSalttrader.retro.core.EventHandler
import il.soulSalttrader.retro.core.content.HistoryScreen
import il.soulSalttrader.retro.core.nav.NavItems.Alerts
import il.soulSalttrader.retro.core.nav.NavItems.Breathe
import il.soulSalttrader.retro.core.nav.NavItems.History
import il.soulSalttrader.retro.core.nav.NavItems.Home
import il.soulSalttrader.retro.core.nav.NavTargetTop

fun NavGraphBuilder.topNavGraph(
    eventHandler: EventHandler,
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
                    BadgeButton.increment(navItems = listOf(Alerts, Breathe, History, Home)),
                    BadgeButton.decrement(navItems = listOf(Alerts, Breathe, History, Home)),
                ),
                leftButtons = listOf(
                    BadgeButton.clear(navItems = listOf(Alerts, Home)),
                    BadgeButton.clearAll()
                ),
                onEvent = eventHandler.onBadge,
            )
        }
    }
}


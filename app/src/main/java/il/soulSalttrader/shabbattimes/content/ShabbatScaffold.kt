package il.soulSalttrader.shabbattimes.content

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import il.soulSalttrader.shabbattimes.nav.NavItem
import il.soulSalttrader.shabbattimes.nav.NavItems.Previous
import il.soulSalttrader.shabbattimes.nav.NavItems.Settings
import il.soulSalttrader.shabbattimes.nav.Navigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShabbatScaffold(
    topNavItems: List<NavItem> = listOf(Settings, Previous),
    navigator: Navigator,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    content: @Composable (PaddingValues) -> Unit,
) {
    val currentNavTarget by navigator.currentTarget.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            NavBarTop(
                navItems = topNavItems,
                currentNavTarget = currentNavTarget,
                navigator = navigator,
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding -> content(innerPadding) }
}
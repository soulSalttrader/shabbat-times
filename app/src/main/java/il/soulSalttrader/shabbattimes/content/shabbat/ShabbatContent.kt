package il.soulSalttrader.shabbattimes.content.shabbat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.content.FabMenu
import il.soulSalttrader.shabbattimes.content.reorderable.SwipeConfig
import il.soulSalttrader.shabbattimes.content.reorderable.rememberReorderableState
import il.soulSalttrader.shabbattimes.content.reorderable.reorderableSection
import il.soulSalttrader.shabbattimes.content.search.CitySearchScreen
import il.soulSalttrader.shabbattimes.content.search.SearchConfig
import il.soulSalttrader.shabbattimes.content.search.SearchItem
import il.soulSalttrader.shabbattimes.content.search.SearchItems.Add
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay

@Composable
fun ShabbatContent(
    halachicTimesDisplay: List<HalachicTimesDisplay>,
    swipeConfig: SwipeConfig<HalachicTimesDisplay> = SwipeConfig(),
    searchConfig: SearchConfig,
    isDraggable: Boolean = true,

    onClick: () -> Unit = {},
) {
    val state = rememberReorderableState(items = halachicTimesDisplay, keyOf = { it.city.id })

    LaunchedEffect(halachicTimesDisplay) {
        state.updateList(halachicTimesDisplay)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            state = state.lazyListState,
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            reorderableSection(
                state = state.reorderableState,
                header = "My locations",
                items = state.list,
                keyOf = { it.city.id },
                swipeConfig = swipeConfig,
            ) { item, modifier ->
                ShabbatCard(
                    modifier = modifier,
                    item = item,
                    isDraggable = isDraggable,
                    locationStatus = item.locationStatus,
                    onClick = { onClick() }
                )
            }
        }

        AnimatedSearchScrim(
            searchActive = searchConfig.state.searchActive,
            onDismiss = { searchConfig.action.onChangeVisibility(false) },
        )

        AnimatedSearchOverlay(searchConfig = searchConfig)

        AnimatedSearchFab(
            searchActive = searchConfig.state.searchActive,
            onToggle = { searchConfig.action.onChangeVisibility(!searchConfig.state.searchActive) },
        )
    }
}

@Composable
private fun BoxScope.AnimatedSearchScrim(
    modifier: Modifier = Modifier,
    searchActive: Boolean,
    onDismiss: () -> Unit,
) {
    AnimatedVisibility(visible = searchActive) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                .clickable { onDismiss() }
        )
    }
}

@Composable
private fun BoxScope.AnimatedSearchOverlay(
    modifier: Modifier = Modifier,
    searchConfig: SearchConfig,
) {
    AnimatedVisibility(
        visible = searchConfig.state.searchActive,
        enter = slideInVertically { -it / 2 } + fadeIn(),
        exit = slideOutVertically { -it / 2 } + fadeOut()
    ) {
        CitySearchScreen(
            searchConfig = searchConfig,
            modifier = modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp),
        )
    }
}

@Composable
private fun BoxScope.AnimatedSearchFab(
    searchActive: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    fabItems: List<SearchItem> = listOf(Add),
) {
    AnimatedVisibility(
        modifier = modifier
            .align(Alignment.BottomEnd)
            .navigationBarsPadding()
            .padding(16.dp),
        visible = !searchActive
    ) {
        FabMenu(
            items = fabItems,
            onClick = { onToggle() },
        )
    }
}
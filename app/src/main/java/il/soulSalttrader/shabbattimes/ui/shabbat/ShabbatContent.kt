package il.soulSalttrader.shabbattimes.ui.shabbat

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.TestTags
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.ui.FabMenu
import il.soulSalttrader.shabbattimes.ui.reorderable.SwipeConfig
import il.soulSalttrader.shabbattimes.ui.reorderable.rememberReorderableState
import il.soulSalttrader.shabbattimes.ui.reorderable.reorderableList
import il.soulSalttrader.shabbattimes.ui.search.LocationSearchScreen
import il.soulSalttrader.shabbattimes.ui.search.SearchConfig
import il.soulSalttrader.shabbattimes.ui.search.SearchItem
import il.soulSalttrader.shabbattimes.ui.search.SearchItems.Add
import il.soulSalttrader.shabbattimes.model.ShabbatEntry
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ShabbatContent(
    items: ImmutableList<ShabbatEntry>,
    swipeConfig: SwipeConfig<ShabbatEntry> = SwipeConfig(),
    searchConfig: SearchConfig,
    isDraggable: Boolean = true,

    onClick: () -> Unit = {},
    onReorder: (from: Int, to: Int) -> Unit = {_, _ ->},
) {
    val state = rememberReorderableState(items = items, onReorder = onReorder)
    val header = stringResource(R.string.shabbat_my_locations)

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            state = state.lazyListState,
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            reorderableList(
                state = state,
                header = header,
                items = state.list,
                keyOf = { it.location.id },
                swipeConfig = swipeConfig,
            ) { item, modifier ->
                ShabbatCard(
                    dragModifier = modifier,
                    testTag = when (item.location.id) {
                        SavedLocation.GPS_ID   -> TestTags.GPS_CARD
                        SavedLocation.EMPTY_ID -> TestTags.EMPTY_CARD
                        else                   -> TestTags.LOCATION_CARD
                    },
                    item = item,
                    isDraggable = isDraggable,
                    onClick = { onClick() }
                )
            }
        }

        AnimatedSearchScrim(searchConfig = searchConfig)
        AnimatedSearchOverlay(searchConfig = searchConfig)
        AnimatedSearchFab(searchConfig = searchConfig)
    }
}

@Composable
private fun BoxScope.AnimatedSearchScrim(
    modifier: Modifier = Modifier,
    searchConfig: SearchConfig
) {
    AnimatedVisibility(visible = searchConfig.state.searchActive) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                .clickable { searchConfig.action.onChangeVisibility(false) }
                .testTag(TestTags.SEARCH_SCRIM)
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
        LocationSearchScreen(
            searchConfig = searchConfig,
            modifier = modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp),
        )
    }
}

@Composable
private fun BoxScope.AnimatedSearchFab(
    modifier: Modifier = Modifier,
    searchConfig: SearchConfig,
    fabItems: List<SearchItem> = listOf(Add),
) {
    AnimatedVisibility(
        modifier = modifier
            .align(Alignment.BottomEnd)
            .navigationBarsPadding()
            .padding(16.dp),
        visible = !searchConfig.state.searchActive,
    ) {
        FabMenu(
            items = fabItems,
            onClick = { searchConfig.action.onChangeVisibility(!searchConfig.state.searchActive) },
        )
    }
}
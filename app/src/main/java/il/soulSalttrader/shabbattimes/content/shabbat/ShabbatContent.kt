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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.content.FabMenu
import il.soulSalttrader.shabbattimes.content.SwipeConfigs
import il.soulSalttrader.shabbattimes.content.SwipeToDismissContainer
import il.soulSalttrader.shabbattimes.content.search.CitySearchScreen
import il.soulSalttrader.shabbattimes.content.search.SearchItem
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.event.PermissionEvent
import il.soulSalttrader.shabbattimes.event.SearchEvent

import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay
import il.soulSalttrader.shabbattimes.content.search.SearchItems.Add
import il.soulSalttrader.shabbattimes.event.ShabbatDataEvent

@Composable
fun ShabbatContent(
    halachicTimesDisplay: List<HalachicTimesDisplay>,
    shabbatDispatch: (AppEvent) -> Unit,

    suggestions: List<City>,
    hasQuery: Boolean,
    searchActive: Boolean,
    searchDispatch: (AppEvent) -> Unit,
) {

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            section(
                header = "My locations",
                items = state.list,
                keyOf = { it.city.id },
                onLeftSwipe = { time -> shabbatDispatch(ShabbatDataEvent.TimeDeleted(time.city)) },
            ) { item ->
                ShabbatCard(
                    modifier = Modifier,
                    item = item,
                    locationStatus = item.locationStatus,
                    onClick = { shabbatDispatch(PermissionEvent.Request) }
                )
            }
        }

        AnimatedSearchScrim(
            searchActive = searchActive,
            onDismiss = { searchDispatch(SearchEvent.SearchVisibilityChanged(false)) },
        )

        AnimatedSearchOverlay(
            suggestions = suggestions,
            hasQuery = hasQuery,
            searchActive = searchActive,
            searchDispatch = searchDispatch,
        )

        AnimatedSearchFab(
            searchActive = searchActive,
            onToggle = { searchDispatch(
                SearchEvent.SearchVisibilityChanged(expanded = !searchActive)
            )},
        )
    }
}

fun LazyListScope.section(
    modifier: Modifier = Modifier,
    items: List<HalachicTimesDisplay>,
    header: String,
    onLeftSwipe: (City) -> Unit,
    onClick: () -> Unit,
) {
    item(header) {
        Text(
            text = header,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }

    items(items, key = { it.city.id }) { time ->
        SwipeToDismissContainer(
            item = time,
            leftSwipe = SwipeConfigs.swipeToDelete(onSwipe = { onLeftSwipe(time.city) }),
        ) {
            ShabbatCard(
                modifier = modifier,
                item = time,
                locationStatus = time.locationStatus,
                onClick = { onClick() }
            )
        }
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
    suggestions: List<City>,
    hasQuery: Boolean,
    searchActive: Boolean,
    searchDispatch: (AppEvent) -> Unit,
) {
    AnimatedVisibility(
        visible = searchActive,
        enter = slideInVertically { -it / 2 } + fadeIn(),
        exit = slideOutVertically { -it / 2 } + fadeOut()
    ) {
        CitySearchScreen(
            suggestions = suggestions,
            hasQuery = hasQuery,
            searchDispatch = searchDispatch,
            expanded = searchActive,
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
package il.soulSalttrader.shabbattimes.content

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
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.event.PermissionEvent
import il.soulSalttrader.shabbattimes.event.SearchEvent
import il.soulSalttrader.shabbattimes.location.LocationStatus
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay
import il.soulSalttrader.shabbattimes.model.SearchItem
import il.soulSalttrader.shabbattimes.model.SearchItems.Add
import il.soulSalttrader.shabbattimes.model.SearchUiState
import il.soulSalttrader.shabbattimes.model.SearchVisibility
import il.soulSalttrader.shabbattimes.model.ShabbatDataState
import il.soulSalttrader.shabbattimes.model.ShabbatUiState

@Composable
fun ShabbatContent(
    shabbatState: ShabbatUiState,
    shabbatDispatch: (AppEvent) -> Unit,

    searchUiState: SearchUiState,
    searchDispatch: (AppEvent) -> Unit,
) {
    val searchActive  = when (searchUiState.visibility) {
        SearchVisibility.Collapsed -> false
        SearchVisibility.Expanded  -> true
    }

    val halachicTimesDisplay = (shabbatState.data as ShabbatDataState.Success).data

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            val grouped = halachicTimesDisplay.groupBy { it.locationStatus is LocationStatus.Current }

            grouped[true]?.let { currentItems ->
                shabbatTimesSection(
                    items = currentItems,
                    key = "current_header",
                    title = "My current location",
                    onClick = { shabbatDispatch(PermissionEvent.Request) },
                )
            }

            grouped[false]?.let { otherItems ->
                if (otherItems.isNotEmpty()) {
                    shabbatTimesSection(
                        items = otherItems,
                        key = "others_header",
                        title = "Other locations",
                        onClick = { shabbatDispatch(PermissionEvent.Request) },
                    )
                }
            }
        }

        AnimatedSearchScrim(
            searchActive = searchActive,
            onDismiss = { searchDispatch(SearchEvent.SearchVisibilityChanged(false)) },
        )

        AnimatedSearchOverlay(
            searchActive = searchActive,
            searchUiState = searchUiState,
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

private fun LazyListScope.shabbatTimesSection(
    items: List<HalachicTimesDisplay>,
    key: String,
    title: String,
    onClick: () -> Unit,
) {
    sectionHeader(key = key, title = title)
    sectionCards(items = items, onClick = { onClick() })
}

private fun LazyListScope.sectionCards(
    modifier: Modifier = Modifier,
    items: List<HalachicTimesDisplay>,
    onClick: () -> Unit,
) {
    items(items, key = { it.city.id }) { time ->
        ShabbatCard(
            modifier = modifier,
            item = time,
            locationStatus = time.locationStatus,
            onClick = { onClick() }
        )
    }
}

private fun LazyListScope.sectionHeader(
    modifier: Modifier = Modifier,
    key: String,
    title: String,
) {
    item(key) {
        Text(
            text = title,
            modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
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
    searchActive: Boolean,
    searchUiState: SearchUiState,
    searchDispatch: (AppEvent) -> Unit,
) {
    AnimatedVisibility(
        visible = searchActive,
        enter = slideInVertically { -it / 2 } + fadeIn(),
        exit = slideOutVertically { -it / 2 } + fadeOut()
    ) {
        TODO("CitySearch composable")
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
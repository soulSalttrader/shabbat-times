package il.soulSalttrader.shabbattimes.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.event.PermissionEvent
import il.soulSalttrader.shabbattimes.location.LocationStatus
import il.soulSalttrader.shabbattimes.model.SearchUiState
import il.soulSalttrader.shabbattimes.model.ShabbatDataState
import il.soulSalttrader.shabbattimes.model.ShabbatUiState

@Composable
fun ShabbatContent(
    shabbatState: ShabbatUiState,
    shabbatDispatch: (AppEvent) -> Unit,

    searchUiState: SearchUiState,
    searchDispatch: (AppEvent) -> Unit,
) {
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
                item("current_header") {
                    Text(
                        text = "My current location",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }

                items(currentItems, key = { it.city.id }) { time ->
                    ShabbatCard(
                        item = time,
                        locationStatus = time.locationStatus,
                        onClick = { shabbatDispatch(PermissionEvent.Request) }
                    )
                }
            }

            grouped[false]?.let { otherItems ->
                if (otherItems.isNotEmpty()) {
                    item("others_header") {
                        Text(
                            "Other locations",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        )
                    }

                    items(otherItems, key = { it.city.id }) { time ->
                        ShabbatCard(
                            item = time,
                            locationStatus = time.locationStatus,
                            onClick = { shabbatDispatch(PermissionEvent.Request) }
                        )
                    }
                }
            }
        }
    }
}
package il.soulSalttrader.shabbattimes.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.location.LocationStatus.Current
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay

@Composable
fun ShabbatContent(
    times: List<HalachicTimesDisplay>,
    onClick: () -> Unit = {},
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        val grouped = times.groupBy { it.locationStatus is Current }

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
                    onClick = onClick
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
                        onClick = onClick
                    )
                }
            }
        }
    }
}
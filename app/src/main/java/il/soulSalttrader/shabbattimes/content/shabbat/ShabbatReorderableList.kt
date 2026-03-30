package il.soulSalttrader.shabbattimes.content.shabbat

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.content.SwipeConfigs
import il.soulSalttrader.shabbattimes.content.SwipeToDismissContainer
import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay

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
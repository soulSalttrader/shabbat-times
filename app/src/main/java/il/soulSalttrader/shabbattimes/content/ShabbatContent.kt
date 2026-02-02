package il.soulSalttrader.shabbattimes.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
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
        items(
            items = times,
            key = { it.city.id },
        ) { time ->

            ShabbatCard(
                item = time,
                locationStatus = time.locationStatus,
                onClick = onClick,
            )
        }
    }
}
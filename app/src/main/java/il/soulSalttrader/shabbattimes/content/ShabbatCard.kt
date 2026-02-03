package il.soulSalttrader.shabbattimes.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.location.LocationStatus
import il.soulSalttrader.shabbattimes.location.getLocationLabels
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay

@Composable
fun ShabbatCard(
    item: HalachicTimesDisplay,
    locationStatus: LocationStatus,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    locationLabel: String = locationStatus.getLocationLabels(),
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = if (item.locationStatus == LocationStatus.Current) CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ) else CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 120.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.city.name,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.weight(1f)
                )
            }

            Row {
                when (locationStatus) {
                    is LocationStatus.Current -> {
                        UiIconLabel(
                            text = locationLabel,
                            icon = UiIcon.Resource(R.drawable.home_pin_24px),
                        )
                    }

                    else                      -> {
                        Text(
                            text = locationLabel,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row {
                Column(modifier = Modifier.weight(1f)) {
                    ShabbatDateTime(
                        label = "Candle Lighting",
                        time = item.candleLightingTime,
                        date = item.candleLightingDate,
                        modifier = modifier.padding(vertical = 4.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    ShabbatDateTime(
                        label = "Havdalah",
                        time = item.havdalahTime,
                        date = item.havdalahDate,
                        modifier = modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ShabbatDateTime(
    label: String,
    time: String,
    date: String,
    modifier: Modifier,
) {
    Text(modifier = modifier, text = label, style = MaterialTheme.typography.labelMedium)
    Text(modifier = modifier, text = time, style = MaterialTheme.typography.headlineLarge)
    Text(modifier = modifier, text = date, style = MaterialTheme.typography.bodyMedium)
}

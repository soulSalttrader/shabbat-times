package il.soulSalttrader.shabbattimes.content.shabbat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.content.uiIcon.UiIcon
import il.soulSalttrader.shabbattimes.content.uiIcon.UiIconLabel
import il.soulSalttrader.shabbattimes.location.LocationStatus
import il.soulSalttrader.shabbattimes.location.LocationStatus.Current
import il.soulSalttrader.shabbattimes.location.getLocationLabels
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay

@Composable
fun ShabbatCard(
    item: HalachicTimesDisplay,
    locationStatus: LocationStatus,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    colors: CardColors = getDefaultCardColors(item.locationStatus),
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    locationLabel: String = locationStatus.getLocationLabels(),
    onClick: () -> Unit = {},
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        shape = shape,
        colors = colors,
        elevation = elevation,
        onClick = onClick,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            CityTitle(item.city.name)

            UiIconLocationLabel(locationStatus, locationLabel)

            Spacer(Modifier.height(16.dp))

            ShabbatKeyTimes(
                candleLightingTime = item.candleLightingTime,
                candleLightingDate = item.candleLightingDate,
                havdalahTime = item.havdalahTime,
                havdalahDate = item.havdalahDate,
            )
        }
    }
}

@Composable
private fun getDefaultCardColors(locationStatus: LocationStatus) = when (locationStatus) {
    Current -> CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    )

    else    -> CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun ShabbatKeyTimes(
    candleLightingTime: String,
    candleLightingDate: String,
    havdalahTime: String,
    havdalahDate: String,
    modifier: Modifier = Modifier,
) {
    Row {
        Column(modifier = Modifier.weight(1f)) {
            ShabbatDateTime(
                label = "Candle Lighting",
                time = candleLightingTime,
                date = candleLightingDate,
                modifier = modifier.padding(vertical = 4.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            ShabbatDateTime(
                label = "Havdalah",
                time = havdalahTime,
                date = havdalahDate,
                modifier = modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun UiIconLocationLabel(
    locationStatus: LocationStatus,
    locationLabel: String,
) {
    Row {
        val icon = when (locationStatus is Current) {
            true -> UiIcon.Resource(R.drawable.home_pin_24px)
            else -> null
        }

        UiIconLabel(
            text = locationLabel,
            icon = icon,
        )
    }
}

@Composable
private fun CityTitle(
    name: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.weight(1f)
        )
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

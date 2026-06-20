package il.soulSalttrader.shabbattimes.ui.shabbat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.TestTags
import il.soulSalttrader.shabbattimes.common.formatDate
import il.soulSalttrader.shabbattimes.common.formatTime
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay.Companion.EMPTY_DATE
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay.Companion.EMPTY_TIME
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay.Companion.NA_TIME
import il.soulSalttrader.shabbattimes.model.LocationStatus
import il.soulSalttrader.shabbattimes.model.ShabbatEntry
import il.soulSalttrader.shabbattimes.model.TimeState
import il.soulSalttrader.shabbattimes.model.TimeState.Unavailable
import il.soulSalttrader.shabbattimes.model.toLabel
import il.soulSalttrader.shabbattimes.ui.DialogButtonAction
import il.soulSalttrader.shabbattimes.ui.ExplanatoryDialog
import il.soulSalttrader.shabbattimes.ui.shabbat.UnavailableReason.Both
import il.soulSalttrader.shabbattimes.ui.shabbat.UnavailableReason.CandleLighting
import il.soulSalttrader.shabbattimes.ui.shabbat.UnavailableReason.Havdalah
import il.soulSalttrader.shabbattimes.ui.uiIcon.UiIcon
import il.soulSalttrader.shabbattimes.ui.uiIcon.UiIconImage
import il.soulSalttrader.shabbattimes.ui.uiIcon.UiIconLabel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun ShabbatCard(
    item: ShabbatEntry,
    modifier: Modifier = Modifier,
    testTag: String = "",
    shape: Shape = RoundedCornerShape(16.dp),
    colors: CardColors = getDefaultCardColors(item.status),
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    isDraggable: Boolean = false,
    onClick: () -> Unit = {},
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .testTag(testTag),
        shape = shape,
        colors = colors,
        elevation = elevation,
        onClick = onClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.padding(16.dp).weight(1f)) {
                LocationTitle(item.location.name)

                UiIconLocationLabel(item.status, item.status.toLabel(), colors)

                Spacer(Modifier.height(16.dp))

                ShabbatKeyTimes(item.times, colors = colors)
            }

            isDraggable.takeIf { it }?.let {
                UiIconImage(
                    modifier = modifier.testTag(TestTags.DRAG_HANDLE),
                    icon = UiIcon.Resource(R.drawable.drag_handle),
                    contentDescription = "dragHandle",
                    contentColor = when (item.status) {
                        LocationStatus.Current -> colors.contentColor
                        else                   -> colors.contentColor
                    },
                )
            }
        }
    }
}

@Composable
private fun getDefaultCardColors(status: LocationStatus) = when (status) {
    is LocationStatus.Current           -> CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    )

    is LocationStatus.LastKnownLocation -> CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    )

    else                                -> CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun ShabbatKeyTimes(
    times: HalachicTimesDisplay?,
    modifier: Modifier = Modifier,
    colors: CardColors,
) {
    val candleState = times?.candleLighting
    val havdalahState = times?.havdalah

    val bothUnavailable = candleState is Unavailable && havdalahState is Unavailable

    Row {
        Column(modifier.weight(1f)) {
            ShabbatTimeColumn(
                timeState = candleState,
                label = stringResource(R.string.shabbat_candle_lighting),
                colors = colors,
                modifier = modifier,
                unavailableReason = when (bothUnavailable) {
                    true -> Both
                    else -> CandleLighting
                },
                showWarningIcon = !bothUnavailable,
            )
        }

        Column(modifier.weight(1f)) {
            ShabbatTimeColumn(
                timeState = havdalahState,
                label = stringResource(R.string.shabbat_havdalah_time),
                colors = colors,
                modifier = modifier,
                unavailableReason = when (bothUnavailable) {
                    true -> Both
                    else -> Havdalah
                },
                showWarningIcon = true,
            )
        }
    }
}

@Composable
private fun ShabbatTimeColumn(
    timeState: TimeState?,
    label: String,
    colors: CardColors,
    modifier: Modifier = Modifier,
    unavailableReason: UnavailableReason,
    showWarningIcon: Boolean,
) {
    when (timeState) {
        is TimeState.Available -> ShabbatDateTimeAvailable(
            label = label,
            time = timeState.time,
            date = timeState.date,
            modifier = modifier.padding(vertical = 4.dp),
        )

        is Unavailable         -> ShabbatDateTimeUnavailable(
            label = label,
            modifier = modifier.padding(vertical = 4.dp),
            colors = colors,
            unavailableReason = unavailableReason,
            showWarningIcon = showWarningIcon,
        )

        else                   -> ShabbatDateTimeAvailable(
            label = label,
            time = null,
            date = null,
            modifier = modifier.padding(vertical = 4.dp),
        )
    }
}

@Composable
private fun UiIconLocationLabel(
    status: LocationStatus,
    label: String,
    colors: CardColors,
) {
    Row(modifier = Modifier.testTag(TestTags.LOCATION_LABEL)) {
        val icon = when (status) {
            is LocationStatus.Current           -> UiIcon.Resource(R.drawable.location_on_24px)
            is LocationStatus.LastKnownLocation -> UiIcon.Resource(R.drawable.pin_history_24)
            is LocationStatus.Unknown           -> UiIcon.Resource(R.drawable.not_listed_location_24)
            is LocationStatus.NoPermission      -> UiIcon.Resource(R.drawable.add_location24)
            else                                -> UiIcon.Resource(R.drawable.map_search_24dp)
        }

        UiIconLabel(
            text = label,
            leadingIcon = icon,
            contentColor = when (status) {
                LocationStatus.Current -> colors.contentColor
                else                   -> colors.contentColor
            },
        )
    }
}

@Composable
private fun LocationTitle(
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
            maxLines = 1,
            modifier = Modifier.weight(1f),
            autoSize = TextAutoSize.StepBased(
                minFontSize = MaterialTheme.typography.headlineSmall.fontSize,
                maxFontSize = MaterialTheme.typography.headlineLarge.fontSize
            )
        )
    }
}

@Composable
private fun ShabbatDateTimeAvailable(
    label: String,
    time: LocalTime?,
    date: LocalDate?,
    modifier: Modifier,
) {
    val context = LocalContext.current

    Text(
        modifier = modifier,
        text = label,
        style = MaterialTheme.typography.labelMedium,
    )

    Text(
        modifier = modifier,
        text = time?.let { context.formatTime(it) } ?: AnnotatedString(EMPTY_TIME),
        style = MaterialTheme.typography.headlineLarge,
    )

    Text(
        modifier = modifier,
        text = date?.let { context.formatDate(it) } ?: EMPTY_DATE,
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
private fun ShabbatDateTimeUnavailable(
    label: String,
    time: String = NA_TIME,
    note: String = stringResource(R.string.unavailable_note),
    colors: CardColors,
    modifier: Modifier,
    unavailableReason: UnavailableReason,
    showWarningIcon: Boolean = true,
) {
    var showDialog by remember { mutableStateOf(false) }

    val icon = when (showWarningIcon) {
        true -> UiIcon.Resource(R.drawable.warning_24dp)
        else -> null
    }

    UiIconLabel(
        modifier = modifier.clickable { showDialog = true },
        text = label,
        style = MaterialTheme.typography.labelMedium,
        trailingIcon = icon,
        contentColor = colors.disabledContentColor,
    )

    Text(
        modifier = modifier,
        text = time,
        style = MaterialTheme.typography.headlineLarge,
    )

    Text(
        modifier = modifier,
        text = note,
        style = MaterialTheme.typography.bodyMedium,
    )

    if (showDialog) {
        val message = when (unavailableReason) {
            Both           -> stringResource(R.string.unavailable_both_body)
            Havdalah       -> stringResource(R.string.unavailable_havdalah_body)
            CandleLighting -> stringResource(R.string.unavailable_candle_lighting_body)
        }

        ExplanatoryDialog(
            message = message,
            title = stringResource(R.string.unavailable_dialog_title),
            confirmAction = DialogButtonAction(
                text = stringResource(R.string.unavailable_dialog_confirm),
                onClick = { showDialog = false },
                color = { MaterialTheme.colorScheme.primary }
            ),
        )
    }
}
package il.soulSalttrader.shabbattimes.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.settings.CandleLightingOffset
import il.soulSalttrader.shabbattimes.settings.HavdalahCriterion
import il.soulSalttrader.shabbattimes.settings.ShabbatPreferences
import il.soulSalttrader.shabbattimes.ui.SectionHeader

@Composable
fun TraditionSection(
    preferences: ShabbatPreferences,
    onCandleOffsetChange: (CandleLightingOffset) -> Unit,
    onHavdalahCriterionChange: (HavdalahCriterion) -> Unit,
    header: String = stringResource(R.string.settings_my_tradition),
) {
    var showCandleSheet by remember { mutableStateOf(false) }
    var showHavdalahSheet by remember { mutableStateOf(false) }

    SettingsSection {
        SectionHeader(header)

        SettingsOptionRow(
            label = stringResource(R.string.tradition_candle_lighting_offset),
            selected = preferences.candleLightingOffset,
            onClick = { showCandleSheet = true },
        )

        SettingsOptionRow(
            label = stringResource(R.string.tradition_havdalah_offset),
            selected = preferences.havdalahCriterion,
            onClick = { showHavdalahSheet = true },
        )
    }

    if (showCandleSheet) {
        SettingsOptionSheet(
            title = stringResource(R.string.tradition_candle_lighting_offset),
            options = CandleLightingOffset.entries,
            selected = preferences.candleLightingOffset,
            onSelect = onCandleOffsetChange,
            onDismiss = { showCandleSheet = false },
        )
    }

    if (showHavdalahSheet) {
        SettingsOptionSheet(
            title = stringResource(R.string.tradition_havdalah_offset),
            options = HavdalahCriterion.entries,
            selected = preferences.havdalahCriterion,
            onSelect = onHavdalahCriterionChange,
            onDismiss = { showHavdalahSheet = false },
        )
    }
}

@Preview
@Composable
fun PreviewTraditionSection() {
    TraditionSection(
        preferences = ShabbatDefaults.PREFERENCES,
        onCandleOffsetChange = {},
        onHavdalahCriterionChange = {},
    )
}

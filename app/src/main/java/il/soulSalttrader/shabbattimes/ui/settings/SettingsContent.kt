package il.soulSalttrader.shabbattimes.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.settings.AboutItemDisplay
import il.soulSalttrader.shabbattimes.settings.CandleLightingOffset
import il.soulSalttrader.shabbattimes.settings.HavdalahCriterion
import il.soulSalttrader.shabbattimes.settings.ShabbatPreferences

@Composable
fun SettingsContent(
    items: List<AboutItemDisplay>,
    preferences: ShabbatPreferences,
    onCandleOffsetChange: (CandleLightingOffset) -> Unit,
    onHavdalahCriterionChange: (HavdalahCriterion) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        TraditionSection(
            preferences = preferences,
            onCandleOffsetChange = onCandleOffsetChange,
            onHavdalahCriterionChange = onHavdalahCriterionChange,
        )
        HorizontalDivider()
        AboutSection(items = items)
        HorizontalDivider()
        SupportSection()
    }
}

package il.soulSalttrader.shabbattimes.ui.settings

import il.soulSalttrader.shabbattimes.settings.CandleLightingOffset
import il.soulSalttrader.shabbattimes.settings.HavdalahCriterion
import il.soulSalttrader.shabbattimes.settings.HavdalahSolarDepression
import il.soulSalttrader.shabbattimes.settings.ShabbatPreferences

object ShabbatDefaults {
    val PREFERENCES = ShabbatPreferences(
        candleLightingOffset = CandleLightingOffset.MIN_18,
        havdalahCriterion = HavdalahCriterion.Solar(HavdalahSolarDepression.DEG_8_5),
    )
}
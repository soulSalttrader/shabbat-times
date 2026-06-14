package il.soulSalttrader.shabbattimes.settings

import androidx.annotation.StringRes
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.ui.settings.SettingsOption

enum class CandleLightingOffset(
    val minutes: Long,
    @param:StringRes override val titleRes: Int,
    @param:StringRes override val descRes: Int,
): SettingsOption {
    MIN_10(10L, R.string.candle_offset_10_title, R.string.candle_offset_10_desc),
    MIN_15(15L, R.string.candle_offset_15_title, R.string.candle_offset_15_desc),
    MIN_18(18L, R.string.candle_offset_18_title, R.string.candle_offset_18_desc),
    MIN_20(20L, R.string.candle_offset_20_title, R.string.candle_offset_20_desc),
    MIN_22(22L, R.string.candle_offset_22_title, R.string.candle_offset_22_desc),
    MIN_30(30L, R.string.candle_offset_30_title, R.string.candle_offset_30_desc),
    MIN_40(40L, R.string.candle_offset_40_title, R.string.candle_offset_40_desc),
    MIN_45(45L, R.string.candle_offset_45_title, R.string.candle_offset_45_desc),
    MIN_50(50L, R.string.candle_offset_50_title, R.string.candle_offset_50_desc);

    override val valueLabel: String = "$minutes minutes"
}
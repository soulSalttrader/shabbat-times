package il.soulSalttrader.shabbattimes.settings

import androidx.annotation.StringRes
import il.soulSalttrader.shabbattimes.R

enum class HavdalahSolarDepression(
    val degrees: Double,
    @param:StringRes val titleRes: Int,
    @param:StringRes val descRes: Int,
) {
    DEG_5_95(5.95, R.string.zman_5_95_title,R.string.zman_5_95_desc),
    DEG_6_48(6.48, R.string.zman_6_48_title,R.string.zman_6_48_desc),
    DEG_7_083(7.083, R.string.zman_7_083_title,R.string.zman_7_083_desc),
    DEG_8_5(8.5, R.string.zman_8_5_title,R.string.zman_8_5_desc),
    DEG_16_1(16.1, R.string.alot_16_1_title,R.string.alot_16_1_desc),
}
package il.soulSalttrader.shabbattimes.settings

import androidx.annotation.StringRes
import il.soulSalttrader.shabbattimes.R

enum class HavdalahFixedOffset(
    val minutes: Long,
    @param:StringRes val titleRes: Int,
    @param:StringRes val descRes: Int,
) {
    RABBEINU_TAM_72(72L, R.string.zman_72_min_title, R.string.zman_72_min_desc)
}
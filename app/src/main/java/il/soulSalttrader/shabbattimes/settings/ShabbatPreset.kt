package il.soulSalttrader.shabbattimes.settings

import androidx.annotation.StringRes
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.settings.JewishCommunity.*

sealed interface ShabbatPreset {
    val candleLightingOffsetMinutes: Long
    val havdalahOffsetMinutes: Long
    val key: JewishCommunity
    @get:StringRes val labelRes: Int

    data object Ashkenazi   : ShabbatPreset {
        override val candleLightingOffsetMinutes: Long = 18L
        override val havdalahOffsetMinutes: Long = 40L
        override val key: JewishCommunity = ASHKENAZI
        override val labelRes: Int = R.string.preset_ashkenazi
    }

    data object Mizrahi    : ShabbatPreset {
        override val candleLightingOffsetMinutes: Long = 15L
        override val havdalahOffsetMinutes: Long = 25L
        override val key: JewishCommunity = MIZRAHI
        override val labelRes: Int = R.string.preset_mizrahi
    }

    data object Sephardic  : ShabbatPreset {
        override val candleLightingOffsetMinutes: Long = 15
        override val havdalahOffsetMinutes: Long = 25
        override val key: JewishCommunity = SEPHARDIC
        override val labelRes: Int = R.string.preset_sephardic
    }

    data object Hasidic : ShabbatPreset {
        override val candleLightingOffsetMinutes: Long = 20L
        override val havdalahOffsetMinutes: Long = 72L
        override val key: JewishCommunity = HASIDIC
        override val labelRes: Int = R.string.preset_hasidic

    }

    data object Jerusalem  : ShabbatPreset {
        override val candleLightingOffsetMinutes: Long = 40L
        override val havdalahOffsetMinutes: Long = 40L
        override val key: JewishCommunity = JERUSALEM
        override val labelRes: Int = R.string.preset_jerusalem
    }

    companion object {
        val all: List<ShabbatPreset> = listOf(
            Ashkenazi, Mizrahi, Sephardic, Hasidic, Jerusalem
        )
        fun fromKey(key: JewishCommunity) = all.find { it.key == key } ?: Ashkenazi
    }
}
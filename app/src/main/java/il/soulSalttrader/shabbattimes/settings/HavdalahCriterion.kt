package il.soulSalttrader.shabbattimes.settings

import il.soulSalttrader.shabbattimes.ui.settings.SettingsOption
import java.text.DecimalFormat

sealed interface HavdalahCriterion : SettingsOption {
    data class Solar(val depression: HavdalahSolarDepression) : HavdalahCriterion {
        override val titleRes = depression.titleRes
        override val descRes = depression.descRes
        override val valueLabel = DecimalFormat("0.###").format(depression.degrees) + "°"
    }

    data class Fixed(val offset: HavdalahFixedOffset) : HavdalahCriterion {
        override val titleRes = offset.titleRes
        override val descRes = offset.descRes
        override val valueLabel = "${offset.minutes} minutes"
    }

    companion object {
        val entries: List<HavdalahCriterion> =
            HavdalahSolarDepression.entries.map(::Solar) + HavdalahFixedOffset.entries.map(::Fixed)
    }
}

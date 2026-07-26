package il.soulSalttrader.shabbattimes.ui.settings

import androidx.annotation.StringRes

interface SettingsOption {
    @get:StringRes val titleRes: Int
    @get:StringRes val descRes: Int
    val valueLabel: String
}
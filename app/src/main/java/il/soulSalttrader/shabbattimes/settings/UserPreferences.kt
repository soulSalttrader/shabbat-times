package il.soulSalttrader.shabbattimes.settings

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.core.DataStore
import il.soulSalttrader.shabbattimes.common.constants.ShabbatOffsets.HILUCH_MIL_MINUTES
import il.soulSalttrader.shabbattimes.common.constants.ShabbatOffsets.TZEIT_HAKOCHAVIM_MINUTES
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        val CANDLE_LIGHTING_KEY = longPreferencesKey("candle_lighting_offset")
        val HAVDALAH_KEY = longPreferencesKey("havdalah_offset")
        const val DEFAULT_TIME_FORMAT = 24
    }

    suspend fun candleLightingOffsetMinutes(): Long =
        dataStore.data.first()[CANDLE_LIGHTING_KEY] ?: HILUCH_MIL_MINUTES

    suspend fun havdalahOffsetMinutes(): Long =
        dataStore.data.first()[HAVDALAH_KEY] ?: TZEIT_HAKOCHAVIM_MINUTES

    suspend fun setCandleLightingOffset(minutes: Long) {
        dataStore.edit { prefs -> prefs[CANDLE_LIGHTING_KEY] = minutes }
    }

    suspend fun setHavdalahOffset(minutes: Long) {
        dataStore.edit { prefs -> prefs[HAVDALAH_KEY] = minutes }
    }
}
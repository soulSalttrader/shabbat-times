package il.soulSalttrader.shabbattimes.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        val SHABBAT_PRESET_KEY = stringPreferencesKey("shabbat_preset")
        const val DEFAULT_TIME_FORMAT = 24
    }

    fun shabbatPreset(): Flow<ShabbatPreset> =
        dataStore.data.map { prefs ->
            prefs[SHABBAT_PRESET_KEY]
                ?.let { runCatching { JewishCommunity.valueOf(it) }.getOrNull() }
                ?.let { ShabbatPreset.fromKey(it) }
                ?: ShabbatPreset.Ashkenazi
        }

    suspend fun setShabbatPreset(preset: ShabbatPreset) {
        dataStore.edit { prefs -> prefs[SHABBAT_PRESET_KEY] = preset.key.name }
    }
}
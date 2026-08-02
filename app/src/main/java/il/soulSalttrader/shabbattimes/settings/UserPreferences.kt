package il.soulSalttrader.shabbattimes.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import il.soulSalttrader.shabbattimes.ui.settings.ShabbatDefaults
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        val CANDLE_OFFSET_KEY = stringPreferencesKey("candle_lighting_offset")
        val HAVDALAH_CRITERION_KEY = stringPreferencesKey("havdalah_criterion")
        const val DEFAULT_TIME_FORMAT = 24
    }

    fun shabbatPreferences(): Flow<ShabbatPreferences> =
        dataStore.data.map { prefs ->
            val candleOffset = prefs[CANDLE_OFFSET_KEY]
                ?.let { runCatching { CandleLightingOffset.valueOf(it) }.getOrNull() }
                ?: ShabbatDefaults.PREFERENCES.candleLightingOffset

            val havdalahCriterion = prefs[HAVDALAH_CRITERION_KEY]
                ?.let { parseHavdalahCriterion(it) }
                ?: ShabbatDefaults.PREFERENCES.havdalahCriterion

            ShabbatPreferences(candleOffset, havdalahCriterion)
        }

    suspend fun setCandleLightingOffset(offset: CandleLightingOffset) {
        dataStore.edit { prefs -> prefs[CANDLE_OFFSET_KEY] = offset.name }
    }

    suspend fun setHavdalahCriterion(criterion: HavdalahCriterion) {
        dataStore.edit { prefs -> prefs[HAVDALAH_CRITERION_KEY] = encodeHavdalahCriterion(criterion) }
    }

    private fun encodeHavdalahCriterion(criterion: HavdalahCriterion): String = when (criterion) {
        is HavdalahCriterion.Solar -> "SOLAR:${criterion.depression.name}"
        is HavdalahCriterion.Fixed -> "FIXED:${criterion.offset.name}"
    }

    private fun parseHavdalahCriterion(raw: String): HavdalahCriterion? {
        val (type, name) = raw.split(":", limit = 2).takeIf { it.size == 2 } ?: return null
        return when (type) {
            "SOLAR" -> runCatching {
                HavdalahSolarDepression.valueOf(name)
            }.getOrNull()?.let { HavdalahCriterion.Solar(it) }

            "FIXED" -> runCatching {
                HavdalahFixedOffset.valueOf(name)
            }.getOrNull()?.let { HavdalahCriterion.Fixed(it) }
            else -> null
        }
    }
}

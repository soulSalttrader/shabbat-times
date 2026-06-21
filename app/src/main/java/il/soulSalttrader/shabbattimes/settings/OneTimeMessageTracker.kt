package il.soulSalttrader.shabbattimes.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class OneTimeMessageTracker @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        private val SHOWN_MESSAGE_KEY = stringSetPreferencesKey("shown_one_time_messages")
    }

    suspend fun hasShown(message: OneTimeMessage): Boolean =
        dataStore.data.first()[SHOWN_MESSAGE_KEY]?.contains(message.key) ?: false

    suspend fun markShown(message: OneTimeMessage) {
        dataStore.edit { prefs ->
            val current = prefs[SHOWN_MESSAGE_KEY] ?: emptySet()
            prefs[SHOWN_MESSAGE_KEY] = current + message.key
        }
    }
}
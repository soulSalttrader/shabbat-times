package il.soulSalttrader.shabbattimes.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import il.soulSalttrader.shabbattimes.model.LocationPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PermissionRepository {

    companion object {
        private val PERMISSION_KEY = stringPreferencesKey("permission_state")
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.data.first()[PERMISSION_KEY]?.let { saved ->
                if (saved == "DeniedPermanently") {
                    _permissionState.value = LocationPermission.DeniedPermanently
                }
                // Granted/Denied resolved live via resolvePermissionEvent
            }
        }
    }

    private val _permissionState = MutableStateFlow<LocationPermission>(LocationPermission.Idle)
    override val permissionState: StateFlow<LocationPermission> = _permissionState

    override fun updatePermissionState(state: LocationPermission) {
        _permissionState.value = state
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { prefs ->
                when (state == LocationPermission.DeniedPermanently) {
                    true -> prefs[PERMISSION_KEY] = "DeniedPermanently"
                    else -> prefs.remove(PERMISSION_KEY)
                }
            }
        }
    }
}
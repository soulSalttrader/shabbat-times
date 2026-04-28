package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.location.LocationPermission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionRepositoryImpl @Inject constructor() : PermissionRepository {

    private val _permissionState = MutableStateFlow<LocationPermission>(LocationPermission.Idle)
    override val permissionState: StateFlow<LocationPermission> = _permissionState

    override fun updatePermissionState(state: LocationPermission) {
        _permissionState.value = state
    }
}
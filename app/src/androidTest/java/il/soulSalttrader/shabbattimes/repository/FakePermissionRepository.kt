package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.model.LocationPermission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakePermissionRepository : PermissionRepository {
    private val _permissionState = MutableStateFlow<LocationPermission>(LocationPermission.Idle)
    override val permissionState: StateFlow<LocationPermission> = _permissionState

    override fun updatePermissionState(state: LocationPermission) {
        _permissionState.value = state
    }
}
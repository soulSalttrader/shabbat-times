package il.soulSalttrader.shabbattimes.ui.viewModel

import il.soulSalttrader.shabbattimes.model.LocationPermission
import il.soulSalttrader.shabbattimes.repository.PermissionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakePermissionRepository : PermissionRepository {
    private val _permissionState = MutableStateFlow<LocationPermission>(LocationPermission.Idle)
    override val permissionState: StateFlow<LocationPermission> = _permissionState

    override fun updatePermissionState(state: LocationPermission) {
        _permissionState.value = state
    }
}
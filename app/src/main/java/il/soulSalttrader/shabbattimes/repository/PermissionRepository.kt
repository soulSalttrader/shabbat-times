package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.model.LocationPermission
import kotlinx.coroutines.flow.StateFlow

interface PermissionRepository {
    val permissionState: StateFlow<LocationPermission>
    fun updatePermissionState(state: LocationPermission)
}
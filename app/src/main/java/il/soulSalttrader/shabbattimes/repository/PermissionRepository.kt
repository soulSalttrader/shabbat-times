package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.permission.PermissionState
import kotlinx.coroutines.flow.Flow

interface PermissionRepository {
    val permissionState: Flow<PermissionState>
    fun updatePermissionState(state: PermissionState)
}
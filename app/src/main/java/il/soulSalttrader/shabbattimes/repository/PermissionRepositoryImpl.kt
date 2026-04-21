package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.permission.PermissionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionRepositoryImpl @Inject constructor() : PermissionRepository {

    private val _permissionState = MutableStateFlow<PermissionState>(PermissionState.Denied)
    override val permissionState: StateFlow<PermissionState> = _permissionState

    override fun updatePermissionState(state: PermissionState) {
        _permissionState.value = state
    }
}
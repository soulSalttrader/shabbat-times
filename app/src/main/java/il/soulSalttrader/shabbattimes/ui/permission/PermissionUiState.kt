package il.soulSalttrader.shabbattimes.ui.permission

import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.model.State

data class PermissionUiState(
    val permission: PermissionState = PermissionState.Idle,
) : State
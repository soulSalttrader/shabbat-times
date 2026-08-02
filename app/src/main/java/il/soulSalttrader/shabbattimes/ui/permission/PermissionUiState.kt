package il.soulSalttrader.shabbattimes.ui.permission

import il.soulSalttrader.shabbattimes.model.State
import il.soulSalttrader.shabbattimes.permission.PermissionState

data class PermissionUiState(
    val permission: PermissionState = PermissionState.Idle,
) : State

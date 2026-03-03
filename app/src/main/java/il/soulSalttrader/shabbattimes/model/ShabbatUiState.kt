package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.location.LocationState
import il.soulSalttrader.shabbattimes.permission.PermissionState

data class ShabbatUiState(
    val data: ShabbatResultState = ShabbatResultState.Idle,
    val permission: PermissionState = PermissionState.Idle,
    val location: LocationState = LocationState.Idle,
) : State
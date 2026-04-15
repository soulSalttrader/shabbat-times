package il.soulSalttrader.shabbattimes.location

import il.soulSalttrader.shabbattimes.model.State
import il.soulSalttrader.shabbattimes.permission.PermissionState

data class LocationUiState(
    val location: LocationState = LocationState.Idle,
    val status: LocationStatus = LocationStatus.Unknown,
    val permission: PermissionState = PermissionState.Idle,
) : State
package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.shabbatApp.permission.PermissionState
import il.soulSalttrader.shabbattimes.model.State
import il.soulSalttrader.shabbattimes.shabbatApp.location.LocationState

data class ShabbatState(
    val data: ShabbatDataState = ShabbatDataState.Idle,
    val permission: PermissionState = PermissionState.Idle,
    val location: LocationState = LocationState.Idle,
) : State
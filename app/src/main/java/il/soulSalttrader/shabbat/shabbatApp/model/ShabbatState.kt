package il.soulSalttrader.retro.shabbatApp.model

import il.soulSalttrader.retro.shabbatApp.permission.PermissionState
import il.soulSalttrader.retro.core.model.State
import il.soulSalttrader.retro.shabbatApp.location.LocationState

data class ShabbatState(
    val data: ShabbatDataState = ShabbatDataState.Idle,
    val permission: PermissionState = PermissionState.Idle,
    val location: LocationState = LocationState.Idle,
) : State
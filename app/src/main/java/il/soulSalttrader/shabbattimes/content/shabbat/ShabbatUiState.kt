package il.soulSalttrader.shabbattimes.content.shabbat

import il.soulSalttrader.shabbattimes.content.Selection
import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.model.State
import il.soulSalttrader.shabbattimes.permission.PermissionState

data class ShabbatUiState(
    val data: ShabbatResultState = ShabbatResultState.Idle,
    val selectedCity: Selection<City?> = Selection.Idle,
    val permission: PermissionState = PermissionState.Idle,
) : State
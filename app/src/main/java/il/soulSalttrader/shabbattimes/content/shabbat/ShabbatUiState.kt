package il.soulSalttrader.shabbattimes.content.shabbat

import il.soulSalttrader.shabbattimes.location.GpsState
import il.soulSalttrader.shabbattimes.model.State

data class ShabbatUiState(
    val data: ShabbatResultState = ShabbatResultState.Idle,
    val gpsState: GpsState = GpsState.Idle,
) : State
package il.soulSalttrader.shabbattimes.ui.gps

import il.soulSalttrader.shabbattimes.model.State

data class GpsUiState(
    val gpsResult: GpsResultState = GpsResultState.Idle,
) : State

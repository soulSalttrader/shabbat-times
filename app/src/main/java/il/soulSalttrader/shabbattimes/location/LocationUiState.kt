package il.soulSalttrader.shabbattimes.location

import il.soulSalttrader.shabbattimes.model.State

data class LocationUiState(
    val data: LocationData = LocationData(),
    val state: LocationState = LocationState.Idle,
    val status: LocationStatus = LocationStatus.Unknown,
) : State
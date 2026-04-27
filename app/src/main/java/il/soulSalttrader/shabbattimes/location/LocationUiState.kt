package il.soulSalttrader.shabbattimes.location

import il.soulSalttrader.shabbattimes.model.State

data class LocationUiState(
    val location: LocationState = LocationState.Idle,
    val status: LocationStatus = LocationStatus.Unknown,
) : State
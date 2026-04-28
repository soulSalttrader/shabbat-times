package il.soulSalttrader.shabbattimes.location

import il.soulSalttrader.shabbattimes.model.State

data class LocationUiState(
    val currentLocation: LocationUiModel? = null,
    val savedLocations: List<LocationUiModel> = emptyList(),
    val gpsState: GpsState = GpsState.Idle,
) : State
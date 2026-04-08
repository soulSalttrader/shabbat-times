package il.soulSalttrader.shabbattimes.event

import il.soulSalttrader.shabbattimes.location.LocationData
import il.soulSalttrader.shabbattimes.location.LocationState
import il.soulSalttrader.shabbattimes.location.LocationStatus
import il.soulSalttrader.shabbattimes.location.LocationUiState
import il.soulSalttrader.shabbattimes.reducer.LocationReducer
import il.soulSalttrader.shabbattimes.reducer.Reducible
import il.soulSalttrader.shabbattimes.repository.SeedCities

sealed interface LocationEvent : AppEvent, Reducible<LocationUiState> {

    data object LocationLoaded : LocationEvent {
        override val reducer = LocationReducer { state ->
            state.copy(
                state = when (state.data.city) {
                    SeedCities.NONE -> LocationState.Idle
                    null            -> LocationState.Unavailable("City not found")
                    else            -> LocationState.Current(location = LocationData(city = state.data.city))
                },
                data = when (state.data.city) {
                    SeedCities.NONE, null -> LocationData()
                    else            -> LocationData(city = state.data.city)
                },
                status = when (state.data.city) {
                    SeedCities.NONE, null -> LocationStatus.Unknown
                    else            -> LocationStatus.Current
                },
            )
        }
    }
}
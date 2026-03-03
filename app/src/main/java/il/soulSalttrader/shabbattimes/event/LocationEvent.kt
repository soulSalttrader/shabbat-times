package il.soulSalttrader.shabbattimes.event

import il.soulSalttrader.shabbattimes.location.LocationData
import il.soulSalttrader.shabbattimes.location.LocationState
import il.soulSalttrader.shabbattimes.content.shabbat.ShabbatUiState
import il.soulSalttrader.shabbattimes.reducer.Reducible
import il.soulSalttrader.shabbattimes.reducer.ShabbatReducer

sealed interface LocationEvent : AppEvent, Reducible<ShabbatUiState> {

    data object Load : LocationEvent {
        override val reducer = ShabbatReducer { state -> state.copy(location = LocationState.Loading) }
    }

    sealed interface Loaded : LocationEvent {
        data class Success(val location: LocationData) : Loaded {
            override val reducer = ShabbatReducer { state ->
                state.copy(location = LocationState.Current(location))
            }
        }

        data class Failure(val message: String, val cause: Throwable? = null) : Loaded {
            override val reducer = ShabbatReducer { state ->
                state.copy(location = LocationState.Unavailable(message, cause))
            }
        }
    }
}
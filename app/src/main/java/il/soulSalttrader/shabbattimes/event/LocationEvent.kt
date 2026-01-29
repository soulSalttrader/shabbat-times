package il.soulSalttrader.shabbattimes.event

import il.soulSalttrader.shabbattimes.reducer.Reducible
import il.soulSalttrader.shabbattimes.reducer.Reducer
import il.soulSalttrader.shabbattimes.shabbatApp.location.LocationData
import il.soulSalttrader.shabbattimes.shabbatApp.location.LocationState
import il.soulSalttrader.shabbattimes.shabbatApp.model.ShabbatState


sealed interface LocationEvent : AppEvent, Reducible<ShabbatState> {

    data object Load : LocationEvent {
        override val reducer = Reducer { state -> state.copy(location = LocationState.Loading) }
    }

    sealed interface Loaded : LocationEvent {
        data class Success(val location: LocationData) : Loaded {
            override val reducer =
                Reducer { state -> state.copy(location = LocationState.Available(location)) }
        }

        data class Failure(val message: String, val cause: Throwable? = null) : Loaded {
            override val reducer = Reducer { state ->
                state.copy(
                    location = LocationState.Unavailable(
                        message,
                        cause
                    )
                )
            }
        }
    }
}
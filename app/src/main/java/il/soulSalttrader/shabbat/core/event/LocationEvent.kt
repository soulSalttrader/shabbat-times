package il.soulSalttrader.retro.core.event

import il.soulSalttrader.retro.core.reducer.Reducible
import il.soulSalttrader.retro.core.reducer.ShabbatReducer
import il.soulSalttrader.retro.shabbatApp.location.LocationData
import il.soulSalttrader.retro.shabbatApp.location.LocationState
import il.soulSalttrader.retro.shabbatApp.model.ShabbatState


sealed interface LocationEvent : AppEvent, Reducible<ShabbatState> {

    data object Load : LocationEvent {
        override val reducer = ShabbatReducer { state -> state.copy(location = LocationState.Loading) }
    }

    sealed interface Loaded : LocationEvent {
        data class Success(val location: LocationData) : Loaded {
            override val reducer = ShabbatReducer { state -> state.copy(location = LocationState.Available(location)) }
        }

        data class Failure(val message: String, val cause: Throwable? = null) : Loaded {
            override val reducer = ShabbatReducer { state -> state.copy(location = LocationState.Unavailable(message, cause)) }
        }
    }
}
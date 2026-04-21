package il.soulSalttrader.shabbattimes.event

import il.soulSalttrader.shabbattimes.content.Selection
import il.soulSalttrader.shabbattimes.content.city.CityUiState
import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.reducer.CityReducer
import il.soulSalttrader.shabbattimes.reducer.Reducible

sealed interface CityEvent : AppEvent, Reducible<CityUiState> {
    data class CityDeleted(val city: City) : CityEvent {
        override val reducer = CityReducer { state ->
            state.copy(
                selectedCity = Selection.Selected(city),
            )
        }
    }

    data class CurrentCityLoaded(val city: City) : CityEvent {
        override val reducer = CityReducer { state ->
            state.copy(
                selectedCity = Selection.Selected(city),
            )
        }
    }
}
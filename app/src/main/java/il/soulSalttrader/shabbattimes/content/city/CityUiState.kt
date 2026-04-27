package il.soulSalttrader.shabbattimes.content.city

import il.soulSalttrader.shabbattimes.content.Selection
import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.model.State

data class CityUiState(
    val selectedCity: Selection<City?> = Selection.Idle,
) : State
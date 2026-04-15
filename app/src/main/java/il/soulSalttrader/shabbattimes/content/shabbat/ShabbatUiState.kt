package il.soulSalttrader.shabbattimes.content.shabbat

import il.soulSalttrader.shabbattimes.content.Selection
import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.model.State

data class ShabbatUiState(
    val data: ShabbatResultState = ShabbatResultState.Idle,
    val selectedCity: Selection<City?> = Selection.Idle,
) : State
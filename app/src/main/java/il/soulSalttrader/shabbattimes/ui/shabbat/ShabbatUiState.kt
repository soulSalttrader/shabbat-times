package il.soulSalttrader.shabbattimes.ui.shabbat

import il.soulSalttrader.shabbattimes.model.State

data class ShabbatUiState(
    val shabbat: ShabbatResultState = ShabbatResultState.Idle,
) : State
package il.soulSalttrader.shabbattimes.content.shabbat

import il.soulSalttrader.shabbattimes.model.State

data class ShabbatUiState(
    val shabbat: ShabbatResultState = ShabbatResultState.Idle,
) : State
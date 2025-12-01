package il.soulSalttrader.retro.shabbatApp.content

import il.soulSalttrader.retro.shabbatApp.model.Shabbat

interface ShabbatNetworkResult {
    data object Loading : ShabbatNetworkResult
    data class Success(val data: Shabbat) : ShabbatNetworkResult
    data class Error(val message: String) : ShabbatNetworkResult
}
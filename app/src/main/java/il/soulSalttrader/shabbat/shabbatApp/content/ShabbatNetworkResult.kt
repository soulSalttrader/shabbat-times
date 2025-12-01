package il.soulSalttrader.retro.shabbatApp.content

import il.soulSalttrader.retro.shabbatApp.model.ShabbatResult

interface ShabbatNetworkResult {
    data object Loading : ShabbatNetworkResult
    data class Success(val data: ShabbatResult) : ShabbatNetworkResult
    data class Error(val message: String) : ShabbatNetworkResult
}
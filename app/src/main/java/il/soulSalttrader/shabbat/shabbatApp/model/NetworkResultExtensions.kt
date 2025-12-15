package il.soulSalttrader.retro.shabbatApp.model

import il.soulSalttrader.retro.shabbatApp.network.NetworkResult
import il.soulSalttrader.retro.shabbatApp.viewModel.ShabbatEvent.Loaded.Failure
import il.soulSalttrader.retro.shabbatApp.viewModel.ShabbatEvent.Loaded.Success

fun NetworkResult<HalachicTimesDisplay>.toLoadedEvent() = when (this) {
    is NetworkResult.Success -> Success(display = data)
    is NetworkResult.Failure -> Failure(message = message, cause = cause)
}
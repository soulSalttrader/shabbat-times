package il.soulSalttrader.retro.shabbatApp.model

import android.content.Context
import il.soulSalttrader.retro.shabbatApp.network.NetworkResult
import il.soulSalttrader.retro.shabbatApp.viewModel.ShabbatEvent.Loaded.Failure
import il.soulSalttrader.retro.shabbatApp.viewModel.ShabbatEvent.Loaded.Success

fun NetworkResult<HalachicTimes>.toLoadedEvent(context: Context) = when (this) {
    is NetworkResult.Success -> Success(display = data.toDisplay(context))
    is NetworkResult.Failure -> Failure(message = message, cause = cause)
}
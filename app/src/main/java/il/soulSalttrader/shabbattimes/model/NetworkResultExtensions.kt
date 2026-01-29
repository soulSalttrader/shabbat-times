package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.shabbatApp.network.NetworkResult
import il.soulSalttrader.shabbattimes.event.ShabbatDataEvent.Loaded.Failure
import il.soulSalttrader.shabbattimes.event.ShabbatDataEvent.Loaded.Success

fun NetworkResult<HalachicTimesDisplay>.toLoadedEvent() = when (this) {
    is NetworkResult.Success -> Success(display = data)
    is NetworkResult.Failure -> Failure(message = message, cause = cause)
}
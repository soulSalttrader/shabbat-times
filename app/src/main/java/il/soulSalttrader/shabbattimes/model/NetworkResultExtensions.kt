package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.event.ShabbatDataEvent.Loaded.Failure
import il.soulSalttrader.shabbattimes.event.ShabbatDataEvent.Loaded.Success
import il.soulSalttrader.shabbattimes.network.NetworkResult

fun NetworkResult<HalachicTimesDisplay>.toLoadedEvent() = when (this) {
    is NetworkResult.Success -> Success(display = data)
    is NetworkResult.Failure -> Failure(message = message, cause = cause)
}
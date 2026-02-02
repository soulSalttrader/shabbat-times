package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.event.ShabbatDataEvent
import il.soulSalttrader.shabbattimes.network.NetworkResult

fun List<NetworkResult<HalachicTimesDisplay>>.toLoadedEvent(): ShabbatDataEvent.Loaded =
    filterIsInstance<NetworkResult.Success<HalachicTimesDisplay>>()
        .map { it.data }
        .let(ShabbatDataEvent.Loaded::Success)
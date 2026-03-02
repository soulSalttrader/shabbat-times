package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.event.ShabbatDataEvent
import il.soulSalttrader.shabbattimes.network.NetworkResult

fun List<NetworkResult<HalachicTimesDisplay>>.toLoadedEvent(): ShabbatDataEvent.Loaded =
    filterIsInstance<NetworkResult.Success<HalachicTimesDisplay>>()
        .map { it.data }
        .let(ShabbatDataEvent.Loaded::Success)

inline fun <T, R> NetworkResult<T>.fold(
    onSuccess: (T) -> R,
    onFailure: (NetworkResult.Failure) -> R
): R =
    when (this) {
        is NetworkResult.Success -> onSuccess(data)
        is NetworkResult.Failure -> onFailure(this)
    }

inline fun <T, R> NetworkResult<T>.map(
    transform: (T) -> R
): NetworkResult<R> =
    fold(
        onSuccess = { NetworkResult.Success(transform(it)) },
        onFailure = { it }
    )

inline fun <T> NetworkResult<T>.mapFailure(
    transform: (NetworkResult.Failure) -> NetworkResult.Failure
): NetworkResult<T> =
    when (this) {
        is NetworkResult.Success -> this
        is NetworkResult.Failure -> transform(this)
    }
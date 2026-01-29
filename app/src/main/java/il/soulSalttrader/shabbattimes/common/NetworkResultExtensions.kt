package il.soulSalttrader.shabbattimes.common

import il.soulSalttrader.shabbattimes.network.NetworkResult

inline fun <T> NetworkResult<T>.onSuccess(action: (T) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Success) action(data)
    return this
}

inline fun <T> NetworkResult<T>.onFailure(action: (NetworkResult.Failure) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Failure) action(this)
    return this
}

fun <T> NetworkResult<T>.getOrThrow(): T =
    when (this) {
        is NetworkResult.Success -> data
        is NetworkResult.Failure -> throw RuntimeException(message, cause)
    }

inline fun <T> NetworkResult<T>.getOrElse(onFailure: (NetworkResult.Failure) -> Nothing): T =
    when (this) {
        is NetworkResult.Success -> data
        is NetworkResult.Failure -> onFailure(this)
    }
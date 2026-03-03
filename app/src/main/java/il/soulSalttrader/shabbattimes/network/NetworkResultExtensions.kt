package il.soulSalttrader.shabbattimes.network

import android.util.Log
import il.soulSalttrader.shabbattimes.Debug

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

inline fun <T> NetworkResult<T>.onSuccess(tag: String, action: (T) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Success) {
        if (Debug.enabled) Log.d(tag, "Success: $data")
        action(data)
    }

    return this
}

inline fun <T> NetworkResult<T>.onFailure(tag: String, action: (NetworkResult.Failure) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Failure) {
        if (Debug.enabled) Log.d(tag, "Failure: $message", cause)
        action(this)
    }
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
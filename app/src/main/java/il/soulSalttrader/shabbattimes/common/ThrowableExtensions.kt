package il.soulSalttrader.shabbattimes.common

import il.soulSalttrader.shabbattimes.network.NetworkResult
import retrofit2.HttpException
import java.io.IOException

fun Throwable.userMessage(): String =
    when (this) {
        is HttpException -> "HTTP ${code()}"
        is IOException   -> "No internet connection"
        else             -> message ?: "Unknown error"
    }

fun Throwable.asNetworkFailure(): NetworkResult.Failure =
    NetworkResult.Failure(
        message = userMessage(),
        cause = this
    )
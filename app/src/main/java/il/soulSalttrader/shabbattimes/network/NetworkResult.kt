package il.soulSalttrader.shabbattimes.network

sealed interface NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>
    data class Failure(val message: String, val cause: Throwable? = null) : NetworkResult<Nothing>
}
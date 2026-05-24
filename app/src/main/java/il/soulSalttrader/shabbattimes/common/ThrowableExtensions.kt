package il.soulSalttrader.shabbattimes.common

import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.model.SolarTimesException
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.ui.UiText
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

fun Throwable?.userMessage(): UiText {
    val cause = this?.cause ?: this
    return cause?.let {
        when (it) {
            is SolarTimesException.InvalidRequest  -> UiText.Resource(R.string.error_invalid_request)
            is SolarTimesException.InvalidDate     -> UiText.Resource(R.string.error_invalid_date)
            is SolarTimesException.InvalidTimezone -> UiText.Resource(R.string.error_server)
            is SolarTimesException.UnknownError    -> UiText.Resource(R.string.error_server)
            is HttpException                       -> UiText.Resource(R.string.error_server)
            is IOException                         -> UiText.Resource(R.string.error_no_internet)
            is TimeoutException                    -> UiText.Resource(R.string.error_timeout)
            else                                   -> UiText.Resource(R.string.error_unknown)
        }
    } ?: UiText.Resource(R.string.error_unknown)
}


fun Throwable.asNetworkFailure(): NetworkResult.Failure = NetworkResult.Failure(cause = this)
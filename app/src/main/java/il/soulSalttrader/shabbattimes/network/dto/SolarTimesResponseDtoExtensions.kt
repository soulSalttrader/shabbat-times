package il.soulSalttrader.shabbattimes.network.dto

import il.soulSalttrader.shabbattimes.model.SolarTimes
import il.soulSalttrader.shabbattimes.model.SolarTimesException
import il.soulSalttrader.shabbattimes.model.toDomain
import il.soulSalttrader.shabbattimes.network.NetworkResult

fun SolarTimesResponseDto.asNetworkResult(): NetworkResult<SolarTimes> = when (status.uppercase()) {
    "OK"              -> NetworkResult.Success(results.toDomain())
    "INVALID_REQUEST" -> NetworkResult.Failure(SolarTimesException.InvalidRequest(status))
    "INVALID_DATE"    -> NetworkResult.Failure(SolarTimesException.InvalidDate(status))
    "INVALID_TZID"    -> NetworkResult.Failure(SolarTimesException.InvalidTimezone(status))
    else              -> NetworkResult.Failure(SolarTimesException.UnknownError(status))
}
package il.soulSalttrader.shabbattimes.network.dto

import il.soulSalttrader.shabbattimes.model.SolarTimes
import il.soulSalttrader.shabbattimes.model.toDomain
import il.soulSalttrader.shabbattimes.network.NetworkResult

fun SolarTimesResponseDto.asNetworkResult(
    use24HourFormat: Boolean
): NetworkResult<SolarTimes> = when (status.uppercase()) {
    "OK" -> NetworkResult.Success(results.toDomain(use24HourFormat))
    else -> NetworkResult.Failure(status)
}
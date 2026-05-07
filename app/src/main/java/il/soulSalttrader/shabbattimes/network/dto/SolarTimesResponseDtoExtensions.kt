package il.soulSalttrader.shabbattimes.network.dto

import il.soulSalttrader.shabbattimes.model.SolarTimes
import il.soulSalttrader.shabbattimes.model.toDomain
import il.soulSalttrader.shabbattimes.network.NetworkResult

fun SolarTimesResponseDto.asNetworkResult(): NetworkResult<SolarTimes> = when (status.uppercase()) {
    "OK" -> NetworkResult.Success(results.toDomain())
    else -> NetworkResult.Failure(status)
}
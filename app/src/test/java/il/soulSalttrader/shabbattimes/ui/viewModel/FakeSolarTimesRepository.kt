package il.soulSalttrader.shabbattimes.ui.viewModel

import il.soulSalttrader.shabbattimes.model.SolarTimes
import il.soulSalttrader.shabbattimes.model.SolarTimesRequest
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.repository.SolarTimesRepository
import java.time.LocalDate
import java.time.ZoneOffset

class FakeSolarTimesRepository : SolarTimesRepository {
    var callCount = 0
    var result: NetworkResult<SolarTimes> = NetworkResult.Success(
        SolarTimes(
            date = LocalDate.of(2026, 5, 14),
            sunset = null,
            dusk = null,
            nauticalTwilightEnd = null,
            zoneOffset = ZoneOffset.UTC,
        ),
    )

    override suspend fun getSolarTimes(request: SolarTimesRequest): NetworkResult<SolarTimes> {
        callCount++
        return result
    }
}

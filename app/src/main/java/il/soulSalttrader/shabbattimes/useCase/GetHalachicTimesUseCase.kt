package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.network.onSuccess
import il.soulSalttrader.shabbattimes.repository.ShabbatRepository
import jakarta.inject.Inject

class GetHalachicTimesUseCase @Inject constructor(
    private val shabbatRepository: ShabbatRepository,
) {
    suspend operator fun invoke(cities: List<City>): NetworkResult<List<HalachicTimesDisplay>> {
        if (cities.isEmpty()) return NetworkResult.Success(emptyList())

        val results = shabbatRepository.getHalachicTimes(cities)
        val successes = mutableListOf<HalachicTimesDisplay>()

        results.forEach { result ->
            result
                .onSuccess(tag = "GetHalachicTimesUseCase") { halachicTimesDisplay ->
                    successes.add(halachicTimesDisplay)
                }
        }

        return when (successes.isEmpty()) {
            true  -> NetworkResult.Failure(message = "No times available")
            false -> NetworkResult.Success(data = successes)
        }
    }
}
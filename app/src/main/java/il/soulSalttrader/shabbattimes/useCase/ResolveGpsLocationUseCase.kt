package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.network.onFailure
import il.soulSalttrader.shabbattimes.network.onSuccess
import il.soulSalttrader.shabbattimes.repository.GeocodingRepository
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class ResolveGpsLocationUseCase @Inject constructor(
    private val geocodingRepository: GeocodingRepository,
    private val observeGpsLocation: ObserveGpsLocationUseCase,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<ResolvedLocation?> = observeGpsLocation()
        .flatMapLatest { location ->
            location?.let {
                flow {
                    geocodingRepository.reverseGeocode(location)
                        .onSuccess("ResolveGpsLocationUseCase") { emit(it) }
                        .onFailure("ResolveGpsLocationUseCase") { emit(null) }
                }
            } ?: flowOf(null)
        }
}
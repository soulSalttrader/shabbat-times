package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.model.CurrentLocationState
import il.soulSalttrader.shabbattimes.network.onFailure
import il.soulSalttrader.shabbattimes.network.onSuccess
import il.soulSalttrader.shabbattimes.repository.GeocodingRepository
import il.soulSalttrader.shabbattimes.ui.gps.GpsResultState
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
    operator fun invoke(): Flow<GpsResultState> = observeGpsLocation()
        .flatMapLatest { state ->
            when (state) {
                is CurrentLocationState.Idle -> flowOf(GpsResultState.Idle)
                is CurrentLocationState.Fetching -> flowOf(GpsResultState.Loading)
                is CurrentLocationState.Available -> flow {
                    emit(GpsResultState.Loading)
                    geocodingRepository.reverseGeocode(state.coordinates)
                        .onSuccess { emit(GpsResultState.Resolved(it)) }
                        .onFailure { emit(GpsResultState.Failure(it.cause)) }
                }
            }
        }
}

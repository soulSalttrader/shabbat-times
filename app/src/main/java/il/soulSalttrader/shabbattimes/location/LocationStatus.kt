package il.soulSalttrader.shabbattimes.location

import androidx.compose.runtime.Immutable

@Immutable
sealed interface LocationStatus {
    @Immutable
    data object Unknown : LocationStatus
    @Immutable
    data object Current : LocationStatus
    @Immutable
    data object NoPermission : LocationStatus
    @Immutable
    data object Locating : LocationStatus
    @Immutable
    data class Nearby(val distanceKm: Double) : LocationStatus
}
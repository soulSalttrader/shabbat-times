package il.soulSalttrader.shabbattimes.location

sealed interface LocationStatus {
    data object Unknown : LocationStatus
    data object Current : LocationStatus
    data object NoPermission : LocationStatus
    data object Locating : LocationStatus
    data class Nearby(val distanceKm: Double) : LocationStatus
}
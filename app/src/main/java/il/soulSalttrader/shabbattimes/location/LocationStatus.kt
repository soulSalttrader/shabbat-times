package il.soulSalttrader.shabbattimes.location

sealed interface LocationStatus {
    data object Unknown : LocationStatus
    data object Current : LocationStatus
    data class Nearby(val km: Double) : LocationStatus
}
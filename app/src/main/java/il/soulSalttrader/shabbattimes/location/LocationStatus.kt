package il.soulSalttrader.shabbattimes.location

sealed interface LocationStatus {
    data object Unknown : LocationStatus
    data object Current : LocationStatus
    data class Distance(val km: Int) : LocationStatus
}
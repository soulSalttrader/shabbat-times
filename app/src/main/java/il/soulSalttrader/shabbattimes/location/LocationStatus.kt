package il.soulSalttrader.shabbattimes.location

sealed interface LocationStatus {
    data class Current(val label: String = "⚐ Your current location") : LocationStatus
    data class Distance(val km: Int) : LocationStatus
}
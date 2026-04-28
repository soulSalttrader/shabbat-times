package il.soulSalttrader.shabbattimes.content.city

sealed interface CityStatus {
    data object Unknown : CityStatus
    data object Current : CityStatus
    data class Nearby(val km: Double) : CityStatus
}
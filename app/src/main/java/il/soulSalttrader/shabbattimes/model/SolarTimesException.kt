package il.soulSalttrader.shabbattimes.model

sealed class SolarTimesException(message: String) : Exception(message) {
    class InvalidRequest(status: String) : SolarTimesException(status)
    class InvalidDate(status: String) : SolarTimesException(status)
    class InvalidTimezone(status: String) : SolarTimesException(status)
    class UnknownError(status: String) : SolarTimesException(status)
}
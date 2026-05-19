package il.soulSalttrader.shabbattimes.model

sealed interface SaveLocationResult {
    data object Success : SaveLocationResult
    data object LimitReached : SaveLocationResult
}
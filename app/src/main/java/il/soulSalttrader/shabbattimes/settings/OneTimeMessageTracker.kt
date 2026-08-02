package il.soulSalttrader.shabbattimes.settings

interface OneTimeMessageTracker {
    suspend fun hasShown(message: OneTimeMessage): Boolean
    suspend fun markShown(message: OneTimeMessage)
}

package il.soulSalttrader.shabbattimes.settings

class FakeOneTimeMessageTracker : OneTimeMessageTracker {
    private val shown = mutableSetOf<String>()

    override suspend fun hasShown(message: OneTimeMessage): Boolean =
        message.key in shown

    override suspend fun markShown(message: OneTimeMessage) {
        shown.add(message.key)
    }

    fun reset() = shown.clear()
    fun shownMessages(): Set<String> = shown.toSet()
}

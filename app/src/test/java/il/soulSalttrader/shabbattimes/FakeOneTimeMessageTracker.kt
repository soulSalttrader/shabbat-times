package il.soulSalttrader.shabbattimes

import il.soulSalttrader.shabbattimes.settings.OneTimeMessage
import il.soulSalttrader.shabbattimes.settings.OneTimeMessageTracker

class FakeOneTimeMessageTracker : OneTimeMessageTracker {

    private val shownMessages = mutableSetOf<String>()

    override suspend fun hasShown(message: OneTimeMessage): Boolean {
        return shownMessages.contains(message.key)
    }

    override suspend fun markShown(message: OneTimeMessage) {
        shownMessages.add(message.key)
    }

    fun reset() {
        shownMessages.clear()
    }

    fun getShownMessages(): Set<String> = shownMessages.toSet()
}
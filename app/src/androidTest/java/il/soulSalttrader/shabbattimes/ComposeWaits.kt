package il.soulSalttrader.shabbattimes

interface ComposeWaits {
    fun waitForTag(tag: String, timeout: Long = 5000): ComposeWaits
    fun waitForTagToDisappear(tag: String, timeout: Long = 5000): ComposeWaits
}

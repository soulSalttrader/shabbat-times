package il.soulSalttrader.shabbattimes

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag

class BaseWaits(private val rule: ComposeTestRule) : ComposeWaits {
    override fun waitForTag(tag: String, timeout: Long) = apply {
        rule.waitUntil(timeout) {
            runCatching {
                rule.onAllNodesWithTag(tag, true).fetchSemanticsNodes().isNotEmpty()
            }.getOrDefault(false)
        }
    }

    override fun waitForTagToDisappear(tag: String, timeout: Long) = apply {
        rule.waitUntil(timeout) {
            runCatching {
                rule.onAllNodesWithTag(tag, true).fetchSemanticsNodes().isEmpty()
            }.getOrDefault(false)
        }
    }
}
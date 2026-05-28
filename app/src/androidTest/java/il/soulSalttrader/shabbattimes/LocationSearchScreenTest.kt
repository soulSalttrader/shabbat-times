package il.soulSalttrader.shabbattimes

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput

class LocationSearchScreenTest(
    private val rule: ComposeTestRule,
    private val slowModeDelayMs: Long = 0,
) {
    fun openSearch() = apply {
        rule.onNodeWithTag(TestTags.FAB_ADD).performClick()
        slow()

        rule.onNodeWithTag(TestTags.FAB_NEW_LOCATION).assertExists().performClick()
        slow()
    }

    fun typeCity(city: String) = apply {
        rule.onNodeWithTag(TestTags.SEARCH_INPUT).performTextInput(city)
        slow()
    }

    fun waitForSuggestions() = apply {
        rule.waitUntil(timeoutMillis = 6000) {
            rule.onAllNodesWithTag(TestTags.SEARCH_SUGGESTION_ITEM).fetchSemanticsNodes().isNotEmpty()
        }
        slow()
    }

    fun selectSuggestion(index: Int = 0) = apply {
        val suggestions = rule.onAllNodesWithTag(
            TestTags.SEARCH_SUGGESTION_ITEM,
            useUnmergedTree = true,
        )
        val count = suggestions.fetchSemanticsNodes().size

        check(count > 0) { "No search suggestions found" }
        check(index < count) { "Only $count suggestions found, cannot select index $index" }

        suggestions[index].performClick()
        slow()
    }

    fun closeSearch() = apply {
        rule.onNodeWithTag(TestTags.SEARCH_SCRIM, true).performClick()
        slow()
    }

    private fun slow() {
        if (slowModeDelayMs > 0) { Thread.sleep(slowModeDelayMs) }
    }
}
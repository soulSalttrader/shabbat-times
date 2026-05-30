package il.soulSalttrader.shabbattimes

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput

class LocationSearchRobot(private val rule: ComposeTestRule) {
    fun waitUntilGpsCardVisible(timeoutMillis: Long = 5000) = apply {
        rule.waitUntil(timeoutMillis) {
            rule.onAllNodesWithTag(TestTags.GPS_CARD)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    fun openSearch() = apply {
        rule.onNodeWithTag(TestTags.FAB_ADD).performClick()
        rule.waitForIdle()

        rule.onNodeWithTag(TestTags.FAB_NEW_LOCATION).assertExists().performClick()
        rule.waitForIdle()
    }

    fun typeCity(city: String) = apply {
        rule.onNodeWithTag(TestTags.SEARCH_INPUT).performTextInput(city)
        rule.waitForIdle()
    }

    fun waitForSuggestions() = apply {
        rule.waitUntil(timeoutMillis = 6000) {
            rule.onAllNodesWithTag(TestTags.SEARCH_SUGGESTION_ITEM).fetchSemanticsNodes().isNotEmpty()
        }
        rule.waitForIdle()
    }

    fun selectSuggestion(index: Int = 0) = apply {
        val suggestions = rule.onAllNodesWithTag(TestTags.SEARCH_SUGGESTION_ITEM, true)
        val count = suggestions.fetchSemanticsNodes().size

        check(count > 0) { "No search suggestions found" }
        check(index < count) { "Only $count suggestions found, cannot select index $index" }

        suggestions[index].performClick()
        rule.waitForIdle()
    }

    fun closeSearch() = apply {
        rule.onNodeWithTag(TestTags.SEARCH_SCRIM, true).performClick()
        rule.waitForIdle()
    }
}
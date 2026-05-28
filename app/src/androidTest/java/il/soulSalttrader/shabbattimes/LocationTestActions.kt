package il.soulSalttrader.shabbattimes

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput

object LocationTestActions {
    fun ComposeTestRule.waitUntilGpsCardVisible(timeoutMillis: Long = 5000) {
        waitUntil(timeoutMillis) {
            onAllNodesWithTag(TestTags.GPS_CARD)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    fun ComposeTestRule.addLocationBySearch(
        cityName: String,
        slowModeDelayMs: Long = 0,
        selectNthSuggestion: Int = 0,
        closeSearchAfter: Boolean = true,
    ) {
        openNewLocationSearch(slowModeDelayMs)
        searchLocation(cityName, slowModeDelayMs)
        selectSuggestion(selectNthSuggestion, slowModeDelayMs)

        if (closeSearchAfter) {
            closeSearch(slowModeDelayMs)
        }
    }

    private fun ComposeTestRule.openNewLocationSearch(
        slowModeDelayMs: Long = 0,
    ) {
        onNodeWithTag(TestTags.FAB_ADD).performClick()
        slow(slowModeDelayMs)

        onNodeWithTag(TestTags.FAB_NEW_LOCATION).assertExists().performClick()
        slow(slowModeDelayMs)
    }

    private fun ComposeTestRule.searchLocation(
        cityName: String,
        slowModeDelayMs: Long = 0,
    ) {
        onNodeWithTag(TestTags.SEARCH_INPUT).performTextInput(cityName)
        slow(slowModeDelayMs)

        waitUntil(timeoutMillis = 6000) {
            onAllNodesWithTag(TestTags.SEARCH_SUGGESTION_ITEM).fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun ComposeTestRule.selectSuggestion(
        index: Int = 0,
        slowModeDelayMs: Long = 0,
    ) {
        val suggestions = onAllNodesWithTag(TestTags.SEARCH_SUGGESTION_ITEM,true)
        val count = suggestions.fetchSemanticsNodes().size

        require(count > 0) { "No search suggestions found" }
        require(index < count) { "Only $count suggestions found, cannot select index $index" }

        suggestions[index].performClick()
        slow(slowModeDelayMs)
    }

    private fun ComposeTestRule.closeSearch(
        slowModeDelayMs: Long = 0,
    ) {
        onNodeWithTag(TestTags.SEARCH_SCRIM, true).performClick()
        slow(slowModeDelayMs)
    }

    private fun slow(delayMs: Long) {
        if (delayMs > 0) Thread.sleep(delayMs)
    }
}
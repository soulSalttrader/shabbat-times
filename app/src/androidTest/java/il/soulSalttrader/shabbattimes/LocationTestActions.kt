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
        apply {
            // 1. Open FAB
            onNodeWithTag(TestTags.FAB_ADD).performClick()
            Thread.sleep(slowModeDelayMs)

            // 2. Click "New Location"
            onNodeWithTag(TestTags.FAB_NEW_LOCATION)
                .assertExists()
                .performClick()
            Thread.sleep(slowModeDelayMs)

            // 3. Type in search field
            onNodeWithTag(TestTags.SEARCH_INPUT).performTextInput(cityName)
            Thread.sleep(slowModeDelayMs)

            // 4. Wait for suggestions
            waitUntil(timeoutMillis = 6000) {
                onAllNodesWithTag(TestTags.SEARCH_SUGGESTION_ITEM)
                    .fetchSemanticsNodes()
                    .isNotEmpty()
            }
            Thread.sleep(slowModeDelayMs)

            // 5. Select N-th suggestion
            val suggestions = onAllNodesWithTag(TestTags.SEARCH_SUGGESTION_ITEM, useUnmergedTree = true)

            if (suggestions.fetchSemanticsNodes().isEmpty()) {
                throw AssertionError("No search suggestions found for '$cityName'")
            }

            if (selectNthSuggestion >= suggestions.fetchSemanticsNodes().size) {
                throw AssertionError("Only ${suggestions.fetchSemanticsNodes().size} suggestions found, cannot select index $selectNthSuggestion")
            }

            suggestions[selectNthSuggestion].performClick()

            Thread.sleep(slowModeDelayMs)

            // 6. Close search overlay (if needed)
            if (closeSearchAfter) {
                onNodeWithTag(TestTags.SEARCH_SCRIM, useUnmergedTree = true)
                    .performClick()
                Thread.sleep(slowModeDelayMs)
            }
        }
    }
}
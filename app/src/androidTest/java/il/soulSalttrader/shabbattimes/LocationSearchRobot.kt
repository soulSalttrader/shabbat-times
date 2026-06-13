package il.soulSalttrader.shabbattimes

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import il.soulSalttrader.shabbattimes.TestTags.FAB_ADD
import il.soulSalttrader.shabbattimes.TestTags.FAB_NEW_LOCATION
import il.soulSalttrader.shabbattimes.TestTags.SEARCH_INPUT
import il.soulSalttrader.shabbattimes.TestTags.SEARCH_SCRIM
import il.soulSalttrader.shabbattimes.TestTags.SEARCH_SUGGESTION_ITEM

class LocationSearchRobot(
    private val rule: ComposeTestRule,
    private val uiRobot: UiRobot = UiRobot(rule),
) {
    private fun openFabMenu() = apply {
        rule.onNodeWithTag(FAB_ADD).assertExists().performClick()
        rule.waitForIdle()
    }

    fun openSearch() = apply {
        openFabMenu()
        rule.onNodeWithTag(FAB_NEW_LOCATION).assertExists().performClick()
        rule.waitForIdle()
    }

    fun closeSearch() = apply {
        rule.onNodeWithTag(SEARCH_SCRIM, true).assertExists().performClick()
        rule.waitForIdle()
    }

    fun enterCity(city: String) = apply {
        rule.onNodeWithTag(SEARCH_INPUT).assertExists().performTextInput(city)
        rule.waitForIdle()
    }

    fun selectSuggestionAt(index: Int = 0) = apply {
        uiRobot.waitForTag(SEARCH_SUGGESTION_ITEM)
        val suggestions = rule.onAllNodesWithTag(SEARCH_SUGGESTION_ITEM, true)
        val count = suggestions.fetchSemanticsNodes().size

        check(count > 0) { "No search suggestions found" }
        check(index < count) { "Only $count suggestions found, cannot select index $index" }

        suggestions[index].assertExists().performClick()
        rule.waitForIdle()
    }
}
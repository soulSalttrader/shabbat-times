package il.soulSalttrader.shabbattimes.card

import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidTest
import il.soulSalttrader.shabbattimes.BaseInstrumentedTest
import il.soulSalttrader.shabbattimes.LocationSearchRobot
import il.soulSalttrader.shabbattimes.TestTags.DRAG_HANDLE
import il.soulSalttrader.shabbattimes.TestTags.EMPTY_CARD
import il.soulSalttrader.shabbattimes.TestTags.LOCATION_CARD
import org.junit.Test

@HiltAndroidTest
class ShabbatCardTest : BaseInstrumentedTest() {

    override fun setupTest() {
        val packageName = InstrumentationRegistry.getInstrumentation().targetContext.packageName

        InstrumentationRegistry.getInstrumentation().uiAutomation.apply {
            executeShellCommand("pm grant $packageName android.permission.ACCESS_FINE_LOCATION")
            executeShellCommand("pm grant $packageName android.permission.ACCESS_COARSE_LOCATION")
        }
        Thread.sleep(300)
    }

    @Test
    fun `should not show drag handle on Empty card`() {
        composeRule.onNodeWithTag(EMPTY_CARD).assertExists()
        composeRule.onNodeWithTag(DRAG_HANDLE).assertDoesNotExist()
    }

    @Test
    fun `should show Empty card when no locations are saved`() {
        composeRule.onNodeWithTag(EMPTY_CARD).assertExists()
    }

    @Test
    fun `should show drag handle on GPS card`() {
        LocationSearchRobot(composeRule).waitUntilGpsCardVisible()
        composeRule.onNodeWithTag(DRAG_HANDLE, true).assertExists()
    }

    @Test
    fun `should show drag handle on location card`() {
        LocationSearchRobot(composeRule)
            .openSearch()
            .typeCity("Brno")
            .waitForSuggestions()
            .selectSuggestion()
            .closeSearch()

        // assert Brno card exists
        composeRule
            .onNode(
                hasTestTag(LOCATION_CARD)
                    .and(hasText("Brno", substring = true)), useUnmergedTree = false)
            .assertExists()

        // assert drag handle exists within any location card
        composeRule
            .onNode(
                hasTestTag(DRAG_HANDLE)
                    .and(hasAnyAncestor(hasTestTag(LOCATION_CARD))),
                useUnmergedTree = true
            )
            .assertExists()
    }
}
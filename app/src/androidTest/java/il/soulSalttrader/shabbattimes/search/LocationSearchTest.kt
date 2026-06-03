package il.soulSalttrader.shabbattimes.search

import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidTest
import il.soulSalttrader.shabbattimes.BaseInstrumentedTest
import il.soulSalttrader.shabbattimes.LocationSearchRobot
import org.junit.Test

@HiltAndroidTest
class LocationSearchTest : BaseInstrumentedTest() {

    override fun setupTest() {
        val packageName = InstrumentationRegistry.getInstrumentation().targetContext.packageName

        InstrumentationRegistry.getInstrumentation().uiAutomation.apply {
            executeShellCommand("pm grant $packageName android.permission.ACCESS_FINE_LOCATION")
            executeShellCommand("pm grant $packageName android.permission.ACCESS_COARSE_LOCATION")
        }
    }

    @Test
    fun `UI_SEARCH_S1 - should add location from search suggestion`() {
        LocationSearchRobot(composeRule)
            .openSearch()
            .typeCity("Brno")
            .waitForSuggestions()
            .selectSuggestion()
            .closeSearch()
            .waitUntilGpsCardVisible()

        composeRule.onNodeWithText("Brno", substring = true).assertExists()
    }

    @Test
    fun `UI_SEARCH_S2 - should not add location when search closed without selection`() {
        LocationSearchRobot(composeRule)
            .openSearch()
            .typeCity("Brno")
            .waitForSuggestions()
            .closeSearch()
            .waitUntilGpsCardVisible()

        composeRule.onNodeWithText("Brno", substring = true).assertDoesNotExist()
    }
}
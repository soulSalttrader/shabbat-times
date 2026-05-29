package il.soulSalttrader.shabbattimes.search

import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import il.soulSalttrader.shabbattimes.BaseInstrumentedTest
import il.soulSalttrader.shabbattimes.LocationSearchRobot
import org.junit.Before
import org.junit.Test

class LocationSearchTest : BaseInstrumentedTest() {

    @Before
    fun grantPermissions() {
        val packageName = InstrumentationRegistry.getInstrumentation().targetContext.packageName

        InstrumentationRegistry.getInstrumentation().uiAutomation.apply {
            executeShellCommand("pm grant $packageName android.permission.ACCESS_FINE_LOCATION")
            executeShellCommand("pm grant $packageName android.permission.ACCESS_COARSE_LOCATION")
        }
        Thread.sleep(300)
    }

    @Test
    fun `SEARCH_ADD_LOCATION_S1 - should successfully add new location from search suggestion`() {
        LocationSearchRobot(composeRule)
            .openSearch()
            .typeCity("Brno")
            .waitForSuggestions()
            .selectSuggestion()
            .closeSearch()

        composeRule.onNodeWithText("Brno", substring = true).assertExists()
    }
}
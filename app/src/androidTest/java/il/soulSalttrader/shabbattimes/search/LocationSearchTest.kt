package il.soulSalttrader.shabbattimes.search

import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidTest
import il.soulSalttrader.shabbattimes.BaseInstrumentedTest
import il.soulSalttrader.shabbattimes.LocationSearchRobot
import il.soulSalttrader.shabbattimes.TestTags.GPS_CARD
import il.soulSalttrader.shabbattimes.TestTags.LOCATION_CARD
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
            .selectSuggestionAt()
            .closeSearch()
            .assertCardPresented(GPS_CARD)
            .assertCardPresented(LOCATION_CARD)
    }

    @Test
    fun `UI_SEARCH_S2 - should not add location when search closed without selection`() {
        LocationSearchRobot(composeRule)
            .openSearch()
            .typeCity("Brno")
            .closeSearch()
            .assertCardPresented(GPS_CARD)
            .assertCardNotPresented(LOCATION_CARD)
    }
}
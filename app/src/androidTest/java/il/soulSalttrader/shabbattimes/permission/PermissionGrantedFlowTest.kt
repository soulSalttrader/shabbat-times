package il.soulSalttrader.shabbattimes.permission

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidTest
import il.soulSalttrader.shabbattimes.LocationTestActions.addLocationBySearch
import il.soulSalttrader.shabbattimes.LocationTestActions.waitUntilGpsCardVisible
import il.soulSalttrader.shabbattimes.TestTags
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class PermissionGrantedFlowTest : PermissionFlowTestBase() {

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
    fun `app launches without crash`() {
        composeRule.onRoot().assertExists()
    }

    @Test
    fun `should not show drag handle on Empty card`() {
        composeRule.onNodeWithTag(TestTags.EMPTY_CARD).assertExists()
        composeRule.onNodeWithTag(TestTags.DRAG_HANDLE).assertDoesNotExist()
    }

    @Test
    fun `should show Empty card when no locations are saved`() {
        composeRule.onNodeWithTag(TestTags.EMPTY_CARD).assertExists()
    }

    @Test
    fun `should show drag handle on GPS card`() {
        composeRule.waitUntilGpsCardVisible()
        composeRule.onNodeWithTag(TestTags.DRAG_HANDLE, true).assertExists()
    }

    @Test
    fun `should show drag handle on location card`() {
        composeRule.waitUntilGpsCardVisible()
        composeRule.onNodeWithTag(TestTags.DRAG_HANDLE, true).assertExists()
    }

    @Test
    fun `should successfully add new location from search suggestion`() {
        composeRule.addLocationBySearch("Brno")
        composeRule.onNodeWithText("Brno", substring = true).assertExists()
    }

    @Test
    fun `PERM_RESTART_S1 - GPS card visible when permission already granted`() {
        composeRule.waitUntilGpsCardVisible()
        composeRule.onNodeWithTag(TestTags.GPS_CARD).assertExists()
    }
}
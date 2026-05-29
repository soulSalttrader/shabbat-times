package il.soulSalttrader.shabbattimes.permission

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidTest
import il.soulSalttrader.shabbattimes.BaseInstrumentedTest
import il.soulSalttrader.shabbattimes.LocationSearchRobot
import il.soulSalttrader.shabbattimes.TestTags
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class PermissionGrantedFlowTest : BaseInstrumentedTest() {

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
    fun `PERM_RESTART_S1 - GPS card visible when permission already granted`() {
        LocationSearchRobot(composeRule).waitUntilGpsCardVisible()
        composeRule.onNodeWithTag(TestTags.GPS_CARD).assertExists()
    }
}
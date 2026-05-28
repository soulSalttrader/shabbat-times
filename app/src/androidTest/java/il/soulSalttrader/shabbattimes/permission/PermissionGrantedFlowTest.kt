package il.soulSalttrader.shabbattimes.permission

import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import il.soulSalttrader.shabbattimes.TestTags
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class PermissionGrantedFlowTest : BasePermissionFlowTest() {

    @Before
    fun grantPermissions() {
        val packageName = InstrumentationRegistry.getInstrumentation().targetContext.packageName

        InstrumentationRegistry.getInstrumentation().uiAutomation.apply {
            executeShellCommand("pm grant $packageName android.permission.ACCESS_FINE_LOCATION")
            executeShellCommand("pm grant $packageName android.permission.ACCESS_COARSE_LOCATION")
        }
    }

    @Test
    fun `app launches without crash`() {
        composeRule.onRoot().assertExists()
    }

    @Test
    fun `PERM_RESTART_S1 - GPS card visible when permission already granted`() {
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule
                .onAllNodesWithTag(TestTags.GPS_CARD)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
        composeRule
            .onNodeWithTag(TestTags.GPS_CARD)
            .assertExists()
    }
}
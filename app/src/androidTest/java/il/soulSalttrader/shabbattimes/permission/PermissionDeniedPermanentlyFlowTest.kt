package il.soulSalttrader.shabbattimes.permission

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import il.soulSalttrader.shabbattimes.LocationSearchRobot
import il.soulSalttrader.shabbattimes.MainActivity
import il.soulSalttrader.shabbattimes.PermissionRobot
import il.soulSalttrader.shabbattimes.TestTags.GPS_CARD
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class PermissionDeniedPermanentlyFlowTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun revokePermissions() {
        val packageName = InstrumentationRegistry.getInstrumentation().targetContext.packageName

        InstrumentationRegistry.getInstrumentation().uiAutomation.apply {
            executeShellCommand("pm revoke $packageName android.permission.ACCESS_FINE_LOCATION")
            executeShellCommand("pm revoke $packageName android.permission.ACCESS_COARSE_LOCATION")
        }
    }

    @Test
    fun `app launches without crash`() {
        composeRule.onRoot().assertExists()
    }

    @Test
    fun `PERM_RESTART_S1 - GPS card invisible when permission denied permanently`() {
        LocationSearchRobot(composeRule)
        composeRule.onNodeWithTag(GPS_CARD).assertDoesNotExist()
    }

    @Test
    fun `UI_PERM_FRESH_S1 - GPS card appears after granting permission`() {
        PermissionRobot(composeRule)
            .tapEmptyCardToStartFlow()
            .assertEducationDialogVisible()
            .confirmEducationDialog()
            .waitForSystemPermissionDialog()
            .grantSystemPermission()
            .waitForGpsCard()
            .assertGpsCardVisible()
    }
}
package il.soulSalttrader.shabbattimes.permission

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import il.soulSalttrader.shabbattimes.LocationSearchRobot
import il.soulSalttrader.shabbattimes.MainActivity
import il.soulSalttrader.shabbattimes.TestTags.DIALOG_CONFIRM_BUTTON
import il.soulSalttrader.shabbattimes.TestTags.EDUCATION_DIALOG
import il.soulSalttrader.shabbattimes.TestTags.EMPTY_CARD
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
        composeRule
            .onNode(hasTestTag(EMPTY_CARD),  true)
            .assertExists()
            .performClick()

        composeRule
            .onNodeWithTag(EDUCATION_DIALOG)
            .assertExists()

        composeRule
            .onNodeWithTag(DIALOG_CONFIRM_BUTTON)
            .assertExists()
            .performClick()

        composeRule
            .onNodeWithTag(EDUCATION_DIALOG)
            .assertDoesNotExist()

        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        device.wait(Until.hasObject(By.pkg("com.android.permissioncontroller")), 3000)

        val allowButton = device.findObject(By.text("While using the app")) ?: device.findObject(By.text("Allow"))
        allowButton?.click()

        composeRule.waitUntil(5000) {
            composeRule
                .onAllNodesWithTag(GPS_CARD)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeRule.onNodeWithTag(GPS_CARD).assertExists()
    }
}
package il.soulSalttrader.shabbattimes

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import il.soulSalttrader.shabbattimes.TestTags.DENIED_PERMANENTLY_DIALOG
import il.soulSalttrader.shabbattimes.TestTags.DIALOG_CONFIRM_BUTTON
import il.soulSalttrader.shabbattimes.TestTags.EDUCATION_DIALOG
import il.soulSalttrader.shabbattimes.TestTags.EMPTY_CARD
import il.soulSalttrader.shabbattimes.TestTags.GPS_CARD
import il.soulSalttrader.shabbattimes.TestTags.RATIONALE_DIALOG

class PermissionRobot(
    private val rule: ComposeTestRule,
    private val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
) {

    fun tapEmptyCardToStartFlow() = apply {
        rule.waitUntil(3000) {
            rule.onAllNodesWithTag(EMPTY_CARD)
                .fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNode(hasTestTag(EMPTY_CARD), true)
            .assertExists()
            .performClick()
    }

    fun tapGpsCardToStartFlow() = apply {
        rule.waitUntil(3000) {
            rule.onAllNodesWithTag(GPS_CARD)
                .fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag(GPS_CARD)
            .assertExists()
            .performClick()
    }

    fun assertEducationDialogVisible() = apply {
        rule.onNodeWithTag(EDUCATION_DIALOG).assertExists()
    }

    fun confirmEducationDialog() = apply {
        rule.onNodeWithTag(DIALOG_CONFIRM_BUTTON)
            .assertExists()
            .performClick()
        rule.onNodeWithTag(EDUCATION_DIALOG).assertDoesNotExist()
    }

    fun waitForSystemPermissionDialog() = apply {
        device.wait(Until.hasObject(By.pkg("com.android.permissioncontroller")), 1000)
    }

    fun grantSystemPermission() = apply {
        val allowButton = device.findObject(By.text("While using the app"))
            ?: device.findObject(By.text("Allow"))
        allowButton?.click()
    }

    fun denySystemPermission() = apply {
        val denyButton = device.findObject(By.text("Don't allow"))
            ?: device.findObject(By.text("Deny"))
        denyButton?.click()
    }

    fun waitForGpsCard() = apply {
        rule.waitUntil(5000) {
            rule.onAllNodesWithTag(GPS_CARD)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    fun assertGpsCardVisible() = apply {
        rule.onNodeWithTag(GPS_CARD).assertExists()
    }

    fun assertGpsCardNotVisible() = apply {
        rule.onNodeWithTag(GPS_CARD).assertDoesNotExist()
    }

    fun assertSystemDialogAppeared() = apply {
        device.waitForIdle(2000)

        val appeared = device.wait(Until.hasObject(By.text("While using the app")),3000)
        assert(appeared != null) { "System permission dialog did not appear" }
    }

    fun assertRationaleDialogVisible() = apply {
        rule.onNodeWithTag(RATIONALE_DIALOG).assertExists()
    }

    fun assertDeniedPermanentlyDialogVisible() = apply {
        rule.onNodeWithTag(DENIED_PERMANENTLY_DIALOG).assertExists()
    }
}
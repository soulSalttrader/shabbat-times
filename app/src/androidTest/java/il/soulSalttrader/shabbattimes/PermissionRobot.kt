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
import il.soulSalttrader.shabbattimes.TestTags.BUTTON_DIALOG_CONFIRM
import il.soulSalttrader.shabbattimes.TestTags.BUTTON_DIALOG_DISMISS
import il.soulSalttrader.shabbattimes.di.FakePermissionRepositoryModule
import il.soulSalttrader.shabbattimes.model.LocationPermission

class PermissionRobot(
    private val rule: ComposeTestRule,
    private val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
) {
    fun tapCardToStartFlow(testTag: String) = apply {
        rule.waitUntil(3000) {
            rule.onAllNodesWithTag(testTag)
                .fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNode(hasTestTag(testTag), true)
            .assertExists()
            .performClick()
    }

    fun assertAppDialogPresented(testTag: String) = apply {
        rule.onNodeWithTag(testTag).assertExists()
    }

    fun confirmAppDialog(testTag: String) = apply {
        rule.onNodeWithTag(BUTTON_DIALOG_CONFIRM).assertExists().performClick()
        rule.onNodeWithTag(testTag).assertDoesNotExist()
    }

    fun dismissAppDialog(testTag: String) = apply {
        rule.onNodeWithTag(BUTTON_DIALOG_DISMISS).assertExists().performClick()
        rule.onNodeWithTag(testTag).assertDoesNotExist()
    }

    fun addShabbatCard(savedLocationId: String, cityName: String = "Brno") = apply {
        addCard(rule, savedLocationId, cityName)
    }

    fun waitForShabbatCard(testTag: String) = apply {
        rule.waitUntil(5000) {
            rule.onAllNodesWithTag(testTag)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    fun assertShabbatCardPresented(testTag: String) = apply {
        rule.onNodeWithTag(testTag).assertExists()
    }

    fun assertShabbatCardNotPresented(testTag: String) = apply {
        rule.onNodeWithTag(testTag).assertDoesNotExist()
    }

    fun updateFakePermissionRepository(permission: LocationPermission) = apply {
        FakePermissionRepositoryModule
            .fakePermissionRepository
            .updatePermissionState(permission)
    }

    fun waitForSystemPermissionDialog() = apply {
        device.wait(Until.hasObject(By.pkg("com.android.permissioncontroller")), 1000)
    }

    fun grantSystemPermission() = apply {
        val allowButton = device.findObject(By.text("While using the app"))
            ?: device.findObject(By.text("Allow"))
        allowButton?.click()
    }

    fun assertSystemDialogAppeared() = apply {
        device.waitForIdle(2000)

        val appeared = device.wait(Until.hasObject(By.text("While using the app")),3000)
        assert(appeared != null) { "System permission dialog did not appear" }
    }

    fun denySystemPermission() = apply {
        val denyButton = device.findObject(By.text("Don't allow"))
            ?: device.findObject(By.text("Deny"))
        denyButton?.click()
    }
}
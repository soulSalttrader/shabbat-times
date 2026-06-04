package il.soulSalttrader.shabbattimes

import android.os.Environment
import android.util.Log
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
import java.io.File

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

    fun assertSystemDialogAppeared() = apply {
        device.waitForIdle(2000)

        val appeared = device.wait(Until.hasObject(By.text("While using the app")),3000)
        assert(appeared != null) { "System permission dialog did not appear" }
    }

    fun waitForSystemPermissionDialog(timeoutMs: Long = 6000) = apply {
        val appeared = device.wait(
            Until.hasObject(By.pkg("com.android.permissioncontroller")),
            timeoutMs
        )

        if (!appeared) {
            takeScreenshot()
            throw AssertionError("System permission dialog did not appear")
        }
    }

    fun denyPermissionForever() = apply {
        device.findObject(By.textContains("Don't allow"))?.click()
            ?: device.findObject(By.textContains("Deny"))?.click()
            ?: throw AssertionError("Deny button not found")
    }

    fun allowWhileUsingApp() = apply {
        device.findObject(By.textContains("While using the app"))?.click()
            ?: device.findObject(By.textContains("Allow"))?.click()
            ?: throw AssertionError("Allow button not found")
    }

    private fun takeScreenshot(name: String = "screenshot") {
        val fileName = "${name}_${System.currentTimeMillis()}.png"
        val path = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName)

        device.takeScreenshot(path)
        Log.d("PermissionRobot", "Screenshot Saved: $fileName")
    }
}
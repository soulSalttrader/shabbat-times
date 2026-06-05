package il.soulSalttrader.shabbattimes

import android.os.Environment
import android.util.Log
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import java.io.File

class SystemPermissionRobot(
    private val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
) {
    private val tag = "PermissionRobot"
    private val permissionPackage = "com.android.permissioncontroller"
    private val grantDialogID = "$permissionPackage:id/grant_dialog"
    private val allowForegroundID = "$permissionPackage:id/permission_allow_foreground_only_button"
    private val denyID = "$permissionPackage:id/permission_deny_button"

    fun assertSystemDialogAppeared(rule: ComposeTestRule, timeoutMs: Long = 5000) = apply {
        // Tell Compose to stop looking for its hierarchy while the OS dialog is open
        rule.registerIdlingResource(object : androidx.compose.ui.test.IdlingResource {
            override val isIdleNow: Boolean
                get() = true
        })

        Log.d(tag, "Waiting for system dialog: $grantDialogID")

        val appeared = device.wait(
            Until.hasObject(By.res(grantDialogID)),
            timeoutMs
        )

        Log.d(tag, "$appeared")

        assert(appeared) { "System permission dialog ($grantDialogID) did not appear within ${timeoutMs}ms" }
    }

    fun allowWhileUsingApp(rule: ComposeTestRule, timeoutMs: Long = 2000) = apply {
        Log.d(tag, "Attempting to click 'While using the app' button...")

        val allowButton = device.wait(
            Until.findObject(By.res(allowForegroundID)),
            timeoutMs
        )

        if (allowButton != null) {
            allowButton.click()

            // Allow the OS animation to finish closing the dialog
            device.waitForIdle(1000)

            // Force Compose to wait until your app window is active and stable again
            rule.waitForIdle()

            Log.d(tag, "Successfully clicked allow button.")
        } else {
            takeScreenshot(device)
            throw AssertionError("Could not find button with resource ID: $allowForegroundID")
        }
    }

    fun denyPermission(rule: ComposeTestRule, timeoutMs: Long = 2000) = apply {
        Log.d(tag, "Attempting to click 'Deny' button...")

        val denyButton = device.wait(
            Until.findObject(By.res(denyID)),
            timeoutMs
        )

        if (denyButton != null) {
            denyButton.click()

            device.waitForIdle(1000)

            rule.waitForIdle()

            Log.d(tag, "Successfully clicked deny button.")
        } else {
            takeScreenshot(device)
            throw AssertionError("Could not find button with resource ID: $denyID")
        }
    }

    private fun takeScreenshot(device: UiDevice, name: String = "screenshot") {
        val fileName = "${name}_${System.currentTimeMillis()}.png"
        val path = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName)

        device.takeScreenshot(path)
        Log.d("PermissionRobot", "Screenshot Saved: $fileName")
    }
}
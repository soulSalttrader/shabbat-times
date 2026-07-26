package il.soulSalttrader.shabbattimes

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.performClick
import il.soulSalttrader.shabbattimes.TestTags.BUTTON_DIALOG_CONFIRM
import il.soulSalttrader.shabbattimes.TestTags.BUTTON_DIALOG_DISMISS
import il.soulSalttrader.shabbattimes.TestTags.RATIONALE_DIALOG
import il.soulSalttrader.shabbattimes.di.FakePermissionRepositoryModule
import il.soulSalttrader.shabbattimes.model.LocationPermission

class PermissionRobot(
    private val rule: ComposeTestRule,
    private val uiRobot: UiRobot = UiRobot(rule),
) {
    fun tapCardToStartPermissionFlow(testTag: String) = apply {
        uiRobot.waitForTag(testTag)
        rule.onNode(hasTestTag(testTag), true).assertExists().performClick()
        rule.waitForIdle()
    }

    fun updateFakePermissionRepository(permission: LocationPermission) = apply {
        FakePermissionRepositoryModule
            .fakePermissionRepository
            .updatePermissionState(permission)
    }

    fun assertAppDialogAppeared(testTag: String = RATIONALE_DIALOG, shouldGrant: Boolean) = apply {
        uiRobot.waitForTag(testTag)
        rule.onNode(hasTestTag(testTag), true).assertExists()
        when (shouldGrant) {
            true -> rule.onNode(hasTestTag(BUTTON_DIALOG_CONFIRM)).assertExists().performClick()
            else -> rule.onNode(hasTestTag(BUTTON_DIALOG_DISMISS)).assertExists().performClick()
        }
        rule.waitForIdle()
    }
}
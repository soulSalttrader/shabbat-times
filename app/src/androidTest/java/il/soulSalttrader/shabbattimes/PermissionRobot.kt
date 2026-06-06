package il.soulSalttrader.shabbattimes

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import il.soulSalttrader.shabbattimes.TestTags.BUTTON_DIALOG_CONFIRM
import il.soulSalttrader.shabbattimes.TestTags.BUTTON_DIALOG_DISMISS
import il.soulSalttrader.shabbattimes.di.FakePermissionRepositoryModule
import il.soulSalttrader.shabbattimes.model.LocationPermission

class PermissionRobot(private val rule: ComposeTestRule) {
    fun assertCardPresented(testTag: String) = apply {
        waitForTag(testTag)
        rule.onNodeWithTag(testTag).assertExists()
    }

    fun assertCardNotPresented(testTag: String) = apply {
        waitForTagToDisappear(testTag)
        rule.onNodeWithTag(testTag).assertDoesNotExist()
    }

    fun assertAppDialogPresented(testTag: String) = apply {
        waitForTag(testTag)
        rule.onNodeWithTag(testTag, true).assertExists()
    }

    fun tapCardToStartPermissionFlow(testTag: String) = apply {
        waitForTag(testTag)
        rule.onNode(hasTestTag(testTag), true).assertExists().performClick()
        rule.waitForIdle()
    }

    fun confirmAppDialog(testTag: String) = apply {
        waitForTag(testTag)
        rule.onNodeWithTag(BUTTON_DIALOG_CONFIRM, true).assertExists().performClick()
        rule.onNodeWithTag(testTag, true).assertDoesNotExist()
    }

    fun dismissAppDialog(testTag: String) = apply {
        waitForTag(testTag)
        rule.onNodeWithTag(BUTTON_DIALOG_DISMISS, true).assertExists().performClick()
        rule.onNodeWithTag(testTag, true).assertDoesNotExist()
    }

    fun addShabbatCard(savedLocationId: String, cityName: String = "Brno") = apply {
        addCard(rule, savedLocationId, cityName)
    }

    fun removeShabbatCard(savedLocationId: String, cityName: String = "Brno") = apply {
        removeCard(rule, savedLocationId, cityName)
    }

    fun updateFakePermissionRepository(permission: LocationPermission) = apply {
        FakePermissionRepositoryModule
            .fakePermissionRepository
            .updatePermissionState(permission)
    }

    private fun waitForTag(tag: String, timeout: Long = 5000) = apply {
        rule.waitUntil(timeout) {
            runCatching {
                rule.onAllNodesWithTag(tag, true).fetchSemanticsNodes().isNotEmpty()
            }.getOrDefault(false)
        }
    }

    private fun waitForTagToDisappear(tag: String, timeout: Long = 5000) = apply {
        rule.waitUntil(timeout) {
            runCatching {
                rule.onAllNodesWithTag(tag, true).fetchSemanticsNodes().isEmpty()
            }.getOrDefault(false)
        }
    }
}
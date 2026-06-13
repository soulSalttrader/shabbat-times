package il.soulSalttrader.shabbattimes

import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import il.soulSalttrader.shabbattimes.TestTags.BUTTON_DIALOG_CONFIRM
import il.soulSalttrader.shabbattimes.TestTags.BUTTON_DIALOG_DISMISS
import il.soulSalttrader.shabbattimes.TestTags.DRAG_HANDLE

class UiRobot(
    private val rule: ComposeTestRule,
    private val waits: ComposeWaits = BaseWaits(rule),
) : ComposeWaits by waits {
    fun assertCardPresented(testTag: String) = apply {
        waitForTag(testTag)
        rule.onNodeWithTag(testTag).assertExists()
    }

    fun assertCardNotPresented(testTag: String) = apply {
        waitForTagToDisappear(testTag)
        rule.onNodeWithTag(testTag).assertDoesNotExist()
    }

    fun assertDragHandlePresentedOnCard(testTag: String) = apply {
        waitForTag(testTag)
        rule.onNode(
            hasTestTag(DRAG_HANDLE)
                .and(hasAnyAncestor(hasTestTag(testTag))),
            useUnmergedTree = true
        ).assertExists()
    }

    fun assertDragHandleNotPresentedOnCard(testTag: String) = apply {
        waitForTagToDisappear(testTag)
        rule.onNode(
            hasTestTag(DRAG_HANDLE)
                .and(hasAnyAncestor(hasTestTag(testTag))),
            useUnmergedTree = true
        ).assertDoesNotExist()
    }

    fun assertAppDialogPresented(testTag: String) = apply {
        waitForTag(testTag)
        rule.onNodeWithTag(testTag, true).assertExists()
    }

    fun assertAppDialogNotPresented(testTag: String) = apply {
        waitForTag(testTag)
        rule.onNodeWithTag(testTag, true).assertDoesNotExist()
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
}
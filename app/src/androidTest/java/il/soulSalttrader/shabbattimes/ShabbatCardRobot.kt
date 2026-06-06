package il.soulSalttrader.shabbattimes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.compose.ui.test.swipeUp
import il.soulSalttrader.shabbattimes.TestTags.BUTTON_DIALOG_CONFIRM
import il.soulSalttrader.shabbattimes.TestTags.BUTTON_DIALOG_DISMISS
import il.soulSalttrader.shabbattimes.TestTags.DRAG_HANDLE

class ShabbatCardRobot(private val rule: ComposeTestRule) {
    fun assertCardPresented(testTag: String) = apply {
        waitForTag(testTag)
        rule.onNodeWithTag(testTag).assertExists()
    }

    fun assertCardNotPresented(testTag: String) = apply {
        waitForTagToDisappear(testTag)
        rule.onNodeWithTag(testTag).assertDoesNotExist()
    }

    fun assertDragHandlePresented() = apply {
        waitForTag(DRAG_HANDLE)
        rule.onNodeWithTag(DRAG_HANDLE).assertExists()
    }

    fun assertAppDialogPresented(testTag: String) = apply {
        waitForTag(testTag)
        rule.onNodeWithTag(testTag).assertExists()
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

    fun dragCardUp(tag: String) = apply {
        waitForTag(tag)

        val handleNode = rule.onNode(hasTestTag(DRAG_HANDLE).and(hasAnyAncestor(hasTestTag(tag))), true,)
        val handleBounds = handleNode.fetchSemanticsNode().boundsInRoot

        rule.onRoot().assertExists().performTouchInput {
            swipeUp(
                startY = handleBounds.center.y,
                endY = handleBounds.center.y - 500f,
                durationMillis = 500,
            )
        }
        rule.waitForIdle()
    }

    fun swipeCardToLeft(cardTag: String) = apply {
        waitForTag(cardTag)
        val node = rule.onNodeWithTag(cardTag)

        node.assertExists().performTouchInput {
            swipe(
                start = Offset(width * 0.70f, height / 2f),
                end = Offset(width * 0.12f, height / 2f),
                durationMillis = 280
            )
        }
        rule.waitForIdle()
    }

    fun addShabbatCard(savedLocationId: String, cityName: String = "Brno") = apply {
        addCard(rule, savedLocationId, cityName)
    }

    fun removeShabbatCard(savedLocationId: String, cityName: String = "Brno") = apply {
        removeCard(rule, savedLocationId, cityName)
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
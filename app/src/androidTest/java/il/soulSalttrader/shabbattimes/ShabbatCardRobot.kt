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
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import il.soulSalttrader.shabbattimes.TestTags.DRAG_HANDLE

class ShabbatCardRobot(
    private val rule: ComposeTestRule,
    private val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
) {
    fun assertCardPresent(testTag: String) = apply {
        rule.onNodeWithTag(testTag).assertExists()
    }

    fun assertCardNotPresent(testTag: String) = apply {
        rule.onNodeWithTag(testTag).assertDoesNotExist()
    }

    fun assertDragHandlePresent() = apply {
        rule.onNodeWithTag(DRAG_HANDLE).assertExists()
    }

    fun assertDragHandleOnCard(testTag: String) = apply {
        rule.onNode(
            hasTestTag(DRAG_HANDLE)
                .and(hasAnyAncestor(hasTestTag(testTag))),
            useUnmergedTree = true
        ).assertExists()
    }

    fun assertDragHandleNotPresentOnCard(testTag: String) = apply {
        rule.onNode(
            hasTestTag(DRAG_HANDLE)
                .and(hasAnyAncestor(hasTestTag(testTag))),
            useUnmergedTree = true
        ).assertDoesNotExist()
    }

    fun addShabbatCard(savedLocationId: String, cityName: String = "Brno") = apply {
        addCard(rule, savedLocationId, cityName)
    }

    fun removeShabbatCard(savedLocationId: String, cityName: String = "Brno") = apply {
        removeCard(rule, savedLocationId, cityName)
    }

    fun waitForTag(tag: String, timeout: Long = 3000) = apply {
        rule.waitUntil(timeout) {
            try {
                rule.onAllNodesWithTag(tag).fetchSemanticsNodes().isNotEmpty()
            } catch (e: IllegalStateException) { false }
        }
    }

    fun dragCardUp(tag: String) = apply {
        waitForTag(tag)

        val handleNode = rule.onNode(hasTestTag(DRAG_HANDLE).and(hasAnyAncestor(hasTestTag(tag))), true,)
        val handleBounds = handleNode.fetchSemanticsNode().boundsInRoot

        rule.onRoot().performTouchInput {
            swipeUp(
                startY = handleBounds.center.y,
                endY = handleBounds.center.y - 500f,
                durationMillis = 500,
            )
        }
        rule.waitForIdle()
    }

    fun swipeCardToDelete(cardTag: String) = apply {
        val node = rule.onNodeWithTag(cardTag)

        node.performTouchInput {
            swipe(
                start = Offset(width * 0.70f, height / 2f),
                end = Offset(width * 0.12f, height / 2f),
                durationMillis = 280
            )
        }
        rule.waitForIdle()
    }

    fun assertAppDialogPresented(testTag: String) = apply {
        rule.waitUntil(3000) {
            rule.onAllNodesWithTag(testTag)
                .fetchSemanticsNodes().isNotEmpty()
        }

        rule.onNodeWithTag(testTag).assertExists()
    }

    fun performClickOnButtonDialog(buttonTag: String) = apply {
        rule.onNodeWithTag(buttonTag).performClick()
    }
}
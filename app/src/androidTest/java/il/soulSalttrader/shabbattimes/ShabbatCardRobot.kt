package il.soulSalttrader.shabbattimes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.compose.ui.test.swipeUp
import il.soulSalttrader.shabbattimes.TestTags.DRAG_HANDLE
import il.soulSalttrader.shabbattimes.TestTags.LOCATION_CARD
import il.soulSalttrader.shabbattimes.TestTags.LOCATION_LABEL

class ShabbatCardRobot(
    private val rule: ComposeTestRule,
    private val uiRobot: UiRobot = UiRobot(rule),
) {
    fun dragCardUp(tag: String) = apply {
        uiRobot.waitForTag(tag)

        val handleNode = rule.onNode(hasTestTag(DRAG_HANDLE).and(hasAnyAncestor(hasTestTag(tag))), true)
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
        uiRobot.waitForTag(cardTag)
        val node = rule.onNodeWithTag(cardTag)

        node.assertExists().performTouchInput {
            swipe(
                start = Offset(width * 0.70f, height / 2f),
                end = Offset(width * 0.12f, height / 2f),
                durationMillis = 280,
            )
        }
        rule.waitForIdle()
    }

    fun assertTextPlaceholdersCount(
        text: String,
        cardTag: String = LOCATION_CARD,
        expectedCount: Int = 2,
        substring: Boolean = true,
    ) = apply {
        rule.onAllNodes(
            hasText(text, substring)
                .and(hasAnyAncestor(hasTestTag(cardTag))),
            useUnmergedTree = true,
        ).assertCountEquals(expectedCount)
    }

    fun assertLocationLabelPresented(cardTag: String) = apply {
        rule.onNode(
            hasTestTag(LOCATION_LABEL)
                .and(hasAnyAncestor(hasTestTag(cardTag))),
            useUnmergedTree = true,
        ).assertExists()
    }
}

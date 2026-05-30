package il.soulSalttrader.shabbattimes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.compose.ui.test.swipeUp
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import il.soulSalttrader.shabbattimes.TestTags.CONFIRM_BUTTON_DIALOG
import il.soulSalttrader.shabbattimes.TestTags.DELETE_CARD_DIALOG
import il.soulSalttrader.shabbattimes.TestTags.DISMISS_BUTTON_DIALOG
import il.soulSalttrader.shabbattimes.TestTags.DRAG_HANDLE
import il.soulSalttrader.shabbattimes.di.FakePersistenceModule
import il.soulSalttrader.shabbattimes.model.Coordinates
import il.soulSalttrader.shabbattimes.model.SavedLocation
import kotlinx.coroutines.runBlocking
import java.time.ZoneId

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
        runBlocking {
            FakePersistenceModule.fakeSavedLocations.save(
                SavedLocation(
                    id = savedLocationId,
                    name = cityName,
                    coordinates = Coordinates(0.0, 0.0),
                    timeZoneId = ZoneId.systemDefault(),
                )
            )
        }
    }

    fun dragCardUp(tag: String) = apply {
        rule.onNodeWithTag(tag).performTouchInput {
            swipeUp(
                startY = centerY,
                endY = centerY - 300f,
                durationMillis = 500
            )
        }
    }

    fun swipeCardToDelete(testTag: String) = apply {
        val node = rule.onNodeWithTag(testTag)

        node.performTouchInput {
            swipe(
                start = Offset(width * 0.70f, height / 2f),
                end = Offset(width * 0.12f, height / 2f),
                durationMillis = 280
            )
        }
        rule.waitForIdle()
    }

    fun assertSwipeCardToDeleteDialog() = apply {
        rule.waitUntil(3000) {
            rule.onAllNodesWithTag(DELETE_CARD_DIALOG)
                .fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag(DELETE_CARD_DIALOG).assertExists()
    }

    fun confirmDeleteDialog() = apply {
        rule.onNodeWithTag(CONFIRM_BUTTON_DIALOG).performClick()
    }

    fun dismissDeleteDialog() = apply {
        rule.onNodeWithTag(DISMISS_BUTTON_DIALOG).performClick()
    }
}
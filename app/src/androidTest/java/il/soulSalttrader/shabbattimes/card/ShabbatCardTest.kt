package il.soulSalttrader.shabbattimes.card

import android.util.Log
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidTest
import il.soulSalttrader.shabbattimes.BaseInstrumentedTest
import il.soulSalttrader.shabbattimes.PermissionRobot
import il.soulSalttrader.shabbattimes.ShabbatCardRobot
import il.soulSalttrader.shabbattimes.TestTags
import il.soulSalttrader.shabbattimes.TestTags.BUTTON_DIALOG_CONFIRM
import il.soulSalttrader.shabbattimes.TestTags.BUTTON_DIALOG_DISMISS
import il.soulSalttrader.shabbattimes.TestTags.EMPTY_CARD
import il.soulSalttrader.shabbattimes.TestTags.GPS_CARD
import il.soulSalttrader.shabbattimes.TestTags.LOCATION_CARD
import il.soulSalttrader.shabbattimes.TestTags.SWIPE_CARD_DIALOG
import il.soulSalttrader.shabbattimes.di.FakePersistenceModule
import il.soulSalttrader.shabbattimes.model.SavedLocation
import org.junit.Ignore
import org.junit.Test

@HiltAndroidTest
class ShabbatCardTest : BaseInstrumentedTest() {

    override fun setupTest() {
        val packageName = InstrumentationRegistry.getInstrumentation().targetContext.packageName

        InstrumentationRegistry.getInstrumentation().uiAutomation.apply {
            executeShellCommand("pm grant $packageName android.permission.ACCESS_FINE_LOCATION")
            executeShellCommand("pm grant $packageName android.permission.ACCESS_COARSE_LOCATION")
        }
        Thread.sleep(300)
    }

    @Test
    fun `UI_CARD_S1 - should show empty card when no locations saved`() {
        ShabbatCardRobot(composeRule)
            .assertCardPresent(EMPTY_CARD)
    }

    @Test
    fun `UI_CARD_S2_2 - should show GPS card on relaunch when permission granted`() {
        PermissionRobot(composeRule)
            .waitForShabbatCard(GPS_CARD)
            .assertShabbatCardPresented(GPS_CARD)
    }

    @Test
    fun `UI_CARD_SWIPE_S1 - should show delete confirmation dialog when swiped left`() {
        ShabbatCardRobot(composeRule)
            .addShabbatCard(SavedLocation.LOCATION_ID)
            .assertCardPresent(LOCATION_CARD)
            .swipeCardToDelete(LOCATION_CARD)
            .assertAppDialogPresented(SWIPE_CARD_DIALOG)
    }

    @Test
    fun `UI_CARD_SWIPE_S2 - should remove card when delete confirmed`() {
        ShabbatCardRobot(composeRule)
            .addShabbatCard(SavedLocation.LOCATION_ID)
            .assertCardPresent(LOCATION_CARD)
            .swipeCardToDelete(LOCATION_CARD)
            .assertAppDialogPresented(SWIPE_CARD_DIALOG)
            .performClickOnButtonDialog(BUTTON_DIALOG_CONFIRM)
            .assertCardNotPresent(LOCATION_CARD)
    }

    @Test
    fun `UI_CARD_SWIPE_S3 - should keep card when delete dismissed`() {
        ShabbatCardRobot(composeRule)
            .addShabbatCard(SavedLocation.LOCATION_ID)
            .assertCardPresent(LOCATION_CARD)
            .swipeCardToDelete(LOCATION_CARD)
            .assertAppDialogPresented(SWIPE_CARD_DIALOG)
            .performClickOnButtonDialog(BUTTON_DIALOG_DISMISS)
            .assertCardPresent(LOCATION_CARD)
    }

    @Test
    fun `UI_CARD_SWIPE_S4 - should remove GPS card when swiped and confirmed`() {
        ShabbatCardRobot(composeRule)
            .addShabbatCard(SavedLocation.GPS_ID, "My gps city")
            .assertCardPresent(GPS_CARD)
            .swipeCardToDelete(GPS_CARD)
            .assertAppDialogPresented(SWIPE_CARD_DIALOG)
            .performClickOnButtonDialog(BUTTON_DIALOG_CONFIRM)
            .assertCardNotPresent(GPS_CARD)
    }


    @Test
    fun `UI_CARD_CONTENT_S1 - should display location name on card`() {
        ShabbatCardRobot(composeRule)
            .addShabbatCard(SavedLocation.GPS_ID, "My gps city name")
            .assertCardPresent(GPS_CARD)

        composeRule.onNode(
            hasText("My gps city name")
                .and(hasAnyAncestor(hasTestTag(GPS_CARD))),
            true
        ).assertExists()
    }

    @Test
    fun `UI_CARD_CONTENT_S2 - should display shabbat times on card`() {
        ShabbatCardRobot(composeRule)
            .addShabbatCard(SavedLocation.LOCATION_ID, "Location")
            .assertCardPresent(LOCATION_CARD)

        composeRule.onAllNodes(
            hasText("--:--")
                .and(hasAnyAncestor(hasTestTag(LOCATION_CARD))),
            useUnmergedTree = true
        ).assertCountEquals(2)

        composeRule.onAllNodes(
            hasText("dd/mm/yyyy")
                .and(hasAnyAncestor(hasTestTag(LOCATION_CARD))),
            useUnmergedTree = true
        ).assertCountEquals(2)

        composeRule.onNode(
            hasText("Candle Lighting", substring = true)
                .and(hasAnyAncestor(hasTestTag(LOCATION_CARD))),
            useUnmergedTree = true
        ).assertExists()

        composeRule.onNode(
            hasText("Havdalah Time", substring = true)
                .and(hasAnyAncestor(hasTestTag(LOCATION_CARD))),
            useUnmergedTree = true
        ).assertExists()
    }

    @Test
    fun `UI_CARD_CONTENT_S3 - should show current location label on GPS card`() {
        ShabbatCardRobot(composeRule)
            .waitForTag(GPS_CARD)
            .assertCardPresent(GPS_CARD)

        composeRule.onNode(
            hasTestTag(TestTags.LOCATION_LABEL)
                .and(hasAnyAncestor(hasTestTag(GPS_CARD))),
            useUnmergedTree = true
        ).assertExists()
    }

    @Test
    fun `UI_CARD_CONTENT_S4 - should show add location prompt on empty card`() {
        ShabbatCardRobot(composeRule)
            .removeShabbatCard(GPS_CARD)
            .assertCardNotPresent(GPS_CARD)
            .assertCardPresent(EMPTY_CARD)

        composeRule.onNode(
            hasText("Tap to use current location", true)
                .and(hasAnyAncestor(hasTestTag(EMPTY_CARD))),
            useUnmergedTree = true
        ).assertExists()
    }

    @Test
    fun `UI_CARD_CONTENT_S5 - should show drag handle on GPS card`() {
        ShabbatCardRobot(composeRule)
            .addShabbatCard(SavedLocation.GPS_ID, "My gps city")
            .assertDragHandleOnCard(GPS_CARD)
    }

    @Test
    fun `UI_CARD_CONTENT_S6 - should show drag handle on location card`() {
        ShabbatCardRobot(composeRule)
            .addShabbatCard(LOCATION_CARD)
            .assertDragHandleOnCard(LOCATION_CARD)
    }

    @Test
    fun `UI_CARD_CONTENT_S7 - should not show drag handle on empty card`() {
        ShabbatCardRobot(composeRule)
            .assertCardPresent(EMPTY_CARD)
            .assertDragHandleNotPresentOnCard(EMPTY_CARD)
    }

    @Ignore("ReorderableItem uses custom pointer input not triggerable via performTouchInput. Re-enable when Compose test framework supports drag-and-drop gestures reliably. See UI_CARD_REORDER_S1 in ui_scenarios.md")
    @Test
    fun `UI_CARD_REORDER_S1 - drag card up changes order`() {
        ShabbatCardRobot(composeRule)
            .addShabbatCard(SavedLocation.GPS_ID, "gps")
            .addShabbatCard(SavedLocation.LOCATION_ID, "location")
            .assertDragHandleOnCard(LOCATION_CARD)
            .dragCardUp(LOCATION_CARD)

        assert(FakePersistenceModule.fakeSavedLocations.reorderCalled) {
            "reorder() was never called — drag gesture didn't trigger reorder"
        }

        val locations = FakePersistenceModule.fakeSavedLocations.locations.value
        Log.d("REORDER", "order: ${locations.map { it.name }}")
        assert(locations[0].id == "location")
        assert(locations[1].id == "gps")
    }
}
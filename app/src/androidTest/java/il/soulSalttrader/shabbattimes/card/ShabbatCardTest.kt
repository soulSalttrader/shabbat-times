package il.soulSalttrader.shabbattimes.card

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidTest
import il.soulSalttrader.shabbattimes.BaseInstrumentedTest
import il.soulSalttrader.shabbattimes.ShabbatCardRobot
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
    fun `UI_CARD_S1 - Empty card shown when no locations saved`() {
        ShabbatCardRobot(composeRule)
            .assertCardPresent(EMPTY_CARD)
    }

    @Test
    fun `UI_CARD_CONTENT_S5 - Drag handle visible on GPS card`() {
        ShabbatCardRobot(composeRule)
            .addShabbatCard(SavedLocation.GPS_ID, "My gps city")
            .assertDragHandleOnCard(GPS_CARD)
    }

    @Test
    fun `UI_CARD_CONTENT_S6 - Drag handle visible on location card`() {
        ShabbatCardRobot(composeRule)
            .addShabbatCard(LOCATION_CARD)
            .assertDragHandleOnCard(LOCATION_CARD)
    }

    @Test
    fun `UI_CARD_SWIPE_S1 - swipe left shows delete confirmation dialog`() {
        ShabbatCardRobot(composeRule)
            .addShabbatCard(SavedLocation.LOCATION_ID)
            .assertCardPresent(LOCATION_CARD)
            .swipeCardToDelete(LOCATION_CARD)
            .assertAppDialogPresented(SWIPE_CARD_DIALOG)
    }

    @Test
    fun `UI_CARD_SWIPE_S2 - confirm delete removes card`() {
        ShabbatCardRobot(composeRule)
            .addShabbatCard(SavedLocation.LOCATION_ID)
            .assertCardPresent(LOCATION_CARD)
            .swipeCardToDelete(LOCATION_CARD)
            .assertAppDialogPresented(SWIPE_CARD_DIALOG)
            .performClickOnButtonDialog(BUTTON_DIALOG_CONFIRM)
            .assertCardNotPresent(LOCATION_CARD)
    }

    @Test
    fun `UI_CARD_SWIPE_S3 - dismiss delete keeps card`() {
        ShabbatCardRobot(composeRule)
            .addShabbatCard(SavedLocation.LOCATION_ID)
            .assertCardPresent(LOCATION_CARD)
            .swipeCardToDelete(LOCATION_CARD)
            .assertAppDialogPresented(SWIPE_CARD_DIALOG)
            .performClickOnButtonDialog(BUTTON_DIALOG_DISMISS)
            .assertCardPresent(LOCATION_CARD)
    }

    @Test
    fun `UI_CARD_SWIPE_S4 - GPS card swipe removes GPS card`() {
        ShabbatCardRobot(composeRule)
            .addShabbatCard(SavedLocation.GPS_ID, "My gps city")
            .assertCardPresent(GPS_CARD)
            .swipeCardToDelete(GPS_CARD)
            .assertAppDialogPresented(SWIPE_CARD_DIALOG)
            .performClickOnButtonDialog(BUTTON_DIALOG_CONFIRM)
            .assertCardNotPresent(GPS_CARD)
    }

    @Test
    fun `UI_CARD_CONTENT_S7 - No drag handle on empty card`() {
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
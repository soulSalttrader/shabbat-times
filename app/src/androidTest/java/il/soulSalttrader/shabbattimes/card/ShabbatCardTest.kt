package il.soulSalttrader.shabbattimes.card

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidTest
import il.soulSalttrader.shabbattimes.BaseInstrumentedTest
import il.soulSalttrader.shabbattimes.ShabbatCardRobot
import il.soulSalttrader.shabbattimes.TestTags.EMPTY_CARD
import il.soulSalttrader.shabbattimes.TestTags.GPS_CARD
import il.soulSalttrader.shabbattimes.TestTags.LOCATION_CARD
import il.soulSalttrader.shabbattimes.TestTags.SWIPE_CARD_DIALOG
import il.soulSalttrader.shabbattimes.UiRobot
import il.soulSalttrader.shabbattimes.di.FakePersistenceModule
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay.Companion.EMPTY_DATE
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay.Companion.EMPTY_TIME
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.model.SavedLocation.Companion.LOCATION_ID
import org.junit.Ignore
import org.junit.Test

@HiltAndroidTest
class ShabbatCardTest : BaseInstrumentedTest() {
    private lateinit var uiRobot: UiRobot
    private lateinit var cardRobot: ShabbatCardRobot

    override fun setupTest() {
        val packageName = InstrumentationRegistry.getInstrumentation().targetContext.packageName

        InstrumentationRegistry.getInstrumentation().uiAutomation.apply {
            executeShellCommand("pm grant $packageName android.permission.ACCESS_FINE_LOCATION")
            executeShellCommand("pm grant $packageName android.permission.ACCESS_COARSE_LOCATION")
        }

        uiRobot = UiRobot(composeRule)
        cardRobot = ShabbatCardRobot(composeRule)
    }

    @Test
    fun `UI_CARD_S1 - should show empty card when no locations saved`() {
        uiRobot.assertCardPresented(EMPTY_CARD)
    }

    @Test
    fun `UI_CARD_S2_1 - should show GPS card when permission granted`() {
        uiRobot.assertCardPresented(GPS_CARD)
    }

    @Test
    fun `UI_CARD_S2_2 - should show GPS card on relaunch when permission granted`() {
        uiRobot.assertCardPresented(GPS_CARD)
    }

    @Test
    fun `UI_CARD_SWIPE_S1 - should show delete confirmation dialog when swiped left`() {
        uiRobot
            .addShabbatCard(LOCATION_ID)
            .assertCardPresented(LOCATION_CARD)
        cardRobot
            .swipeCardToLeft(LOCATION_CARD)
        uiRobot
            .assertAppDialogPresented(SWIPE_CARD_DIALOG)
    }

    @Test
    fun `UI_CARD_SWIPE_S2 - should remove card when delete confirmed`() {
        uiRobot
            .addShabbatCard(LOCATION_ID)
            .assertCardPresented(LOCATION_CARD)
        cardRobot
            .swipeCardToLeft(LOCATION_CARD)
        uiRobot
            .assertAppDialogPresented(SWIPE_CARD_DIALOG)
            .confirmAppDialog(SWIPE_CARD_DIALOG)
            .assertCardNotPresented(LOCATION_CARD)
    }

    @Test
    fun `UI_CARD_SWIPE_S3 - should keep card when delete dismissed`() {
        uiRobot
            .addShabbatCard(LOCATION_ID)
            .assertCardPresented(LOCATION_CARD)
        cardRobot
            .swipeCardToLeft(LOCATION_CARD)
        uiRobot
            .assertAppDialogPresented(SWIPE_CARD_DIALOG)
            .dismissAppDialog(SWIPE_CARD_DIALOG)
            .assertCardPresented(LOCATION_CARD)
    }

    @Test
    fun `UI_CARD_SWIPE_S4 - should remove GPS card when swiped and confirmed`() {
        uiRobot
            .addShabbatCard(SavedLocation.GPS_ID, "My gps city")
            .assertCardPresented(GPS_CARD)
        cardRobot
            .swipeCardToLeft(GPS_CARD)
        uiRobot
            .assertAppDialogPresented(SWIPE_CARD_DIALOG)
            .confirmAppDialog(SWIPE_CARD_DIALOG)
            .assertCardNotPresented(GPS_CARD)
    }

    @Test
    fun `UI_CARD_CONTENT_S1 - should display location name on card`() {
        uiRobot
            .addShabbatCard(SavedLocation.GPS_ID, "My gps city")
            .assertCardPresented(GPS_CARD)
        cardRobot
            .assertTextPlaceholdersCount(
                text = "My gps city",
                cardTag = GPS_CARD,
                expectedCount = 1,
            )
    }

    @Test
    fun `UI_CARD_CONTENT_S2 - should display shabbat times on card`() {
        cardRobot
            .assertTextPlaceholdersCount(
                text = EMPTY_TIME,
                cardTag = EMPTY_CARD,
                expectedCount = 2,
            )
            .assertTextPlaceholdersCount(
                text = EMPTY_DATE,
                cardTag = EMPTY_CARD,
                expectedCount = 2,
            )
            .assertTextPlaceholdersCount(
                text = "Candle Lighting",
                cardTag = EMPTY_CARD,
                expectedCount = 1,
            )
            .assertTextPlaceholdersCount(
                text = "Havdalah Time",
                cardTag = EMPTY_CARD,
                expectedCount = 1,
            )
    }

    @Test
    fun `UI_CARD_CONTENT_S3 - should show current location label on GPS card`() {
        uiRobot.assertCardPresented(GPS_CARD)
        cardRobot.assertLocationLabelPresented(GPS_CARD)
    }

    @Test
    fun `UI_CARD_CONTENT_S4 - should show add location prompt on empty card`() {
        uiRobot
            .removeShabbatCard(GPS_CARD, "My gps city")
            .assertCardNotPresented(GPS_CARD)
            .assertCardPresented(EMPTY_CARD)
        cardRobot
            .assertTextPlaceholdersCount(
                text = "Tap to use current location",
                cardTag = EMPTY_CARD,
                expectedCount = 1,
            )
    }

    @Test
    fun `UI_CARD_CONTENT_S5 - should show drag handle on GPS card`() {
        uiRobot
            .addShabbatCard(SavedLocation.GPS_ID, "My gps city")
            .assertDragHandlePresentedOnCard(GPS_CARD)
    }

    @Test
    fun `UI_CARD_CONTENT_S6 - should show drag handle on location card`() {
        uiRobot
            .addShabbatCard(LOCATION_CARD)
            .assertDragHandlePresentedOnCard(LOCATION_CARD)
    }

    @Test
    fun `UI_CARD_CONTENT_S7 - should not show drag handle on empty card`() {
        uiRobot
            .assertCardPresented(EMPTY_CARD)
            .assertDragHandleNotPresentedOnCard(EMPTY_CARD)
    }

    @Ignore(
        "ReorderableItem uses custom pointer input not triggerable via performTouchInput. " +
            "Re-enable when Compose test framework supports drag-and-drop gestures reliably. " +
            "See UI_CARD_REORDER_S1 in ui_scenarios.md",
    )
    @Test
    fun `UI_CARD_REORDER_S1 - drag card up changes order`() {
        uiRobot
            .addShabbatCard(SavedLocation.GPS_ID, "gps")
            .addShabbatCard(LOCATION_ID, "location")
            .assertDragHandlePresentedOnCard(LOCATION_CARD)
        cardRobot
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

package il.soulSalttrader.shabbattimes

import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import il.soulSalttrader.shabbattimes.TestTags.DRAG_HANDLE
import il.soulSalttrader.shabbattimes.TestTags.EMPTY_CARD
import il.soulSalttrader.shabbattimes.TestTags.GPS_CARD
import il.soulSalttrader.shabbattimes.TestTags.LOCATION_CARD
import il.soulSalttrader.shabbattimes.di.FakePersistenceModule
import il.soulSalttrader.shabbattimes.model.Coordinates
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.model.SavedLocation.Companion.GPS_ID
import kotlinx.coroutines.runBlocking
import java.time.ZoneId

class ShabbatCardRobot(
    private val rule: ComposeTestRule,
    private val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
) {
    fun assertGpsCardVisible() = apply {
        rule.onNodeWithTag(GPS_CARD).assertExists()
    }

    fun assertDragHandlePresent() = apply {
        rule.onNodeWithTag(DRAG_HANDLE).assertExists()
    }

    fun assertEmptyCardVisible() = apply {
        rule.onNodeWithTag(EMPTY_CARD).assertExists()
    }

    fun assertDragHandleOnLocationCard() = apply {
        rule.onNode(
            hasTestTag(DRAG_HANDLE)
                .and(hasAnyAncestor(hasTestTag(LOCATION_CARD))),
            useUnmergedTree = true
        ).assertExists()
    }

    fun assertDragHandleOnGpsCard() = apply {
        rule.onNode(
            hasTestTag(DRAG_HANDLE)
                .and(hasAnyAncestor(hasTestTag(GPS_CARD))),
            useUnmergedTree = true
        ).assertExists()
    }

    fun assertNoDragHandleOnEmptyCard() = apply {
        rule.onNode(
            hasTestTag(DRAG_HANDLE)
                .and(hasAnyAncestor(hasTestTag(EMPTY_CARD))),
            useUnmergedTree = true
        ).assertDoesNotExist()
    }

    fun addGPSShabbatCard() = apply {
        runBlocking {
            FakePersistenceModule.fakeSavedLocations.save(
                SavedLocation(
                    id = GPS_ID,
                    name = "Brno",
                    coordinates = Coordinates(0.0, 0.0),
                    timeZoneId = ZoneId.systemDefault(),
                )
            )
        }
    }

    fun addLocationShabbatCard() = apply {
        runBlocking {
            FakePersistenceModule.fakeSavedLocations.save(
                SavedLocation(
                    id = "Location ID",
                    name = "Brno",
                    coordinates = Coordinates(0.0, 0.0),
                    timeZoneId = ZoneId.systemDefault(),
                )
            )
        }
    }
}
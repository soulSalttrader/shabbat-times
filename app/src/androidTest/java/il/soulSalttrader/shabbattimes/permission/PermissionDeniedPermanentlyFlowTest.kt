package il.soulSalttrader.shabbattimes.permission

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidTest
import il.soulSalttrader.shabbattimes.BaseInstrumentedTest
import il.soulSalttrader.shabbattimes.PermissionRobot
import il.soulSalttrader.shabbattimes.TestTags.GPS_CARD
import il.soulSalttrader.shabbattimes.di.FakePermissionRepositoryModule
import il.soulSalttrader.shabbattimes.di.FakePersistenceModule
import il.soulSalttrader.shabbattimes.model.Coordinates
import il.soulSalttrader.shabbattimes.model.LocationPermission
import il.soulSalttrader.shabbattimes.model.SavedLocation
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.ZoneId

@HiltAndroidTest
class PermissionDeniedPermanentlyFlowTest : BaseInstrumentedTest() {

    override fun setupTest() {
        val packageName = InstrumentationRegistry.getInstrumentation().targetContext.packageName

        InstrumentationRegistry.getInstrumentation().uiAutomation.apply {
            executeShellCommand("pm revoke $packageName android.permission.ACCESS_FINE_LOCATION")
            executeShellCommand("pm revoke $packageName android.permission.ACCESS_COARSE_LOCATION")
        }
    }

    @Test
    fun `app launches without crash`() {
        composeRule.onRoot().assertExists()
    }

    @Test
    fun `UI_PERM_FRESH_S2 - system dialog appears after education dialog when permission permanently denied`() {
        PermissionRobot(composeRule)
            .tapEmptyCardToStartFlow()
            .assertEducationDialogVisible()
            .confirmEducationDialog()
            .waitForSystemPermissionDialog()
            .assertSystemDialogAppeared()
    }

    @Test
    fun `UI_PERM_FRESH_S3 - system dialog appears when permission denied`() {
        FakePermissionRepositoryModule
            .fakePermissionRepository
            .updatePermissionState(LocationPermission.Denied)

        PermissionRobot(composeRule)
            .tapEmptyCardToStartFlow()
            .assertSystemDialogAppeared()
    }

    @Test
    fun `UI_PERM_FRESH_S6 - Permanently denied dialog appears after denying twice`() {
        FakePermissionRepositoryModule
            .fakePermissionRepository
            .updatePermissionState(LocationPermission.DeniedPermanently)

        PermissionRobot(composeRule)
            .tapEmptyCardToStartFlow()
            .assertDeniedPermanentlyDialogVisible()
    }

    @Test
    fun `UI_PERM_RESTART_S2 - Outdated GPS card visible and tapping shows system dialog when denied`() {
        FakePermissionRepositoryModule
            .fakePermissionRepository
            .updatePermissionState(LocationPermission.Denied)

        runBlocking {
            FakePersistenceModule.fakeSavedLocations.save(
                SavedLocation(
                    id = SavedLocation.GPS_ID,
                    name = "Brno",
                    coordinates = Coordinates(0.0, 0.0),
                    timeZoneId = ZoneId.systemDefault(),
                )
            )
        }

        composeRule.onNodeWithTag(GPS_CARD).assertExists()

        PermissionRobot(composeRule)
            .tapGpsCardToStartFlow()
            .assertSystemDialogAppeared()
    }

    @Test
    fun `UI_PERM_RESTART_S3 - Outdated GPS card visible and tapping shows open settings dialog when permanently denied`() {
        FakePermissionRepositoryModule
            .fakePermissionRepository
            .updatePermissionState(LocationPermission.DeniedPermanently)

        runBlocking {
            FakePersistenceModule.fakeSavedLocations.save(
                SavedLocation(
                    id = SavedLocation.GPS_ID,
                    name = "Brno",
                    coordinates = Coordinates(0.0, 0.0),
                    timeZoneId = ZoneId.systemDefault(),
                )
            )
        }

        PermissionRobot(composeRule)
            .tapGpsCardToStartFlow()
            .assertDeniedPermanentlyDialogVisible()

        composeRule.onNodeWithText("Open Settings").assertExists()
    }
}
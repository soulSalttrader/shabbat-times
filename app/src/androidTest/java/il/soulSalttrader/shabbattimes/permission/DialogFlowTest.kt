package il.soulSalttrader.shabbattimes.permission

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidTest
import il.soulSalttrader.shabbattimes.BaseInstrumentedTest
import il.soulSalttrader.shabbattimes.PermissionRobot
import il.soulSalttrader.shabbattimes.TestTags.BUTTON_DIALOG_CONFIRM
import il.soulSalttrader.shabbattimes.TestTags.DENIED_PERMANENTLY_DIALOG
import il.soulSalttrader.shabbattimes.TestTags.BUTTON_DIALOG_DISMISS
import il.soulSalttrader.shabbattimes.TestTags.EDUCATION_DIALOG
import il.soulSalttrader.shabbattimes.TestTags.EMPTY_CARD
import il.soulSalttrader.shabbattimes.TestTags.GPS_CARD
import il.soulSalttrader.shabbattimes.TestTags.LOCATION_CARD
import il.soulSalttrader.shabbattimes.model.LocationPermission
import il.soulSalttrader.shabbattimes.model.SavedLocation
import org.junit.Test

@HiltAndroidTest
class DialogFlowTest : BaseInstrumentedTest() {

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
    fun `UI_DIALOG_S1 - should show system dialog after education dialog when permission denied`() {
        PermissionRobot(composeRule)
            .tapCardToStartFlow(EMPTY_CARD)
            .assertAppDialogPresented(EDUCATION_DIALOG)
            .confirmAppDialog(EDUCATION_DIALOG)
            .waitForSystemPermissionDialog()
            .assertSystemDialogAppeared()
    }

    @Test
    fun `UI_DIALOG_S2 - should show system dialog when permission denied`() {
        PermissionRobot(composeRule)
            .updateFakePermissionRepository(LocationPermission.Denied)
            .tapCardToStartFlow(EMPTY_CARD)
            .assertSystemDialogAppeared()
    }

    // UI_DIALOG_S3 - should show rationale dialog after system dialog denial 🖐️

    // UI_DIALOG_S4 - should show GPS card after allowing via rationale ✅ → see UI_CARD_S2_1

    @Test
    fun `UI_DIALOG_S5 - should show permanently denied dialog after denying twice`() {
        PermissionRobot(composeRule)
            .updateFakePermissionRepository(LocationPermission.DeniedPermanently)
            .tapCardToStartFlow(EMPTY_CARD)
            .assertAppDialogPresented(DENIED_PERMANENTLY_DIALOG)
    }

    @Test
    fun `UI_DIALOG_S6 - should show system dialog when tapping outdated GPS card with denied permission`() {
        PermissionRobot(composeRule)
            .updateFakePermissionRepository(LocationPermission.Denied)
            .addShabbatCard(SavedLocation.GPS_ID, "My gsp city")
            .assertShabbatCardPresented(GPS_CARD)
            .tapCardToStartFlow(GPS_CARD)
            .assertSystemDialogAppeared()
    }

    @Test
    fun `UI_DIALOG_S7 - should show open settings dialog when tapping outdated GPS card with permanently denied permission`() {
        PermissionRobot(composeRule)
            .updateFakePermissionRepository(LocationPermission.DeniedPermanently)
            .addShabbatCard(SavedLocation.GPS_ID, "My gsp city")
            .tapCardToStartFlow(GPS_CARD)
            .assertAppDialogPresented(DENIED_PERMANENTLY_DIALOG)

        composeRule.onNodeWithTag(BUTTON_DIALOG_CONFIRM).assertExists()
        composeRule.onNodeWithTag(BUTTON_DIALOG_DISMISS).assertExists()
    }

    @Test
    fun `UI_DIALOG_S8 - should show Education dialog when card tapped with Idle permission`() {
        PermissionRobot(composeRule)
            .updateFakePermissionRepository(LocationPermission.Idle)
            .tapCardToStartFlow(EMPTY_CARD)
            .assertAppDialogPresented(EDUCATION_DIALOG)
    }

    @Test
    fun `UI_DIALOG_S9 - should keep empty card when Education dialog is dismissed`() {
        PermissionRobot(composeRule)
            .updateFakePermissionRepository(LocationPermission.Idle)
            .tapCardToStartFlow(EMPTY_CARD)
            .assertAppDialogPresented(EDUCATION_DIALOG)
            .dismissAppDialog(EDUCATION_DIALOG)
            .assertShabbatCardPresented(EMPTY_CARD)
            .assertShabbatCardNotPresented(GPS_CARD)
            .assertShabbatCardNotPresented(LOCATION_CARD)
    }
}
package il.soulSalttrader.shabbattimes.permission

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidTest
import il.soulSalttrader.shabbattimes.BaseInstrumentedTest
import il.soulSalttrader.shabbattimes.UiRobot
import il.soulSalttrader.shabbattimes.PermissionRobot
import il.soulSalttrader.shabbattimes.SystemPermissionRobot
import il.soulSalttrader.shabbattimes.TestTags.BUTTON_DIALOG_CONFIRM
import il.soulSalttrader.shabbattimes.TestTags.BUTTON_DIALOG_DISMISS
import il.soulSalttrader.shabbattimes.TestTags.DENIED_PERMANENTLY_DIALOG
import il.soulSalttrader.shabbattimes.TestTags.EDUCATION_DIALOG
import il.soulSalttrader.shabbattimes.TestTags.EMPTY_CARD
import il.soulSalttrader.shabbattimes.TestTags.GPS_CARD
import il.soulSalttrader.shabbattimes.TestTags.LOCATION_CARD
import il.soulSalttrader.shabbattimes.TestTags.RATIONALE_DIALOG
import il.soulSalttrader.shabbattimes.model.LocationPermission
import il.soulSalttrader.shabbattimes.model.SavedLocation
import org.junit.Test

@HiltAndroidTest
class DialogFlowTest : BaseInstrumentedTest() {
    private lateinit var uiRobot: UiRobot
    private lateinit var permissionRobot: PermissionRobot
    private lateinit var systemPermissionRobot: SystemPermissionRobot

    override fun setupTest() {
        val packageName = InstrumentationRegistry.getInstrumentation().targetContext.packageName

        InstrumentationRegistry.getInstrumentation().uiAutomation.apply {
            executeShellCommand("pm revoke $packageName android.permission.ACCESS_FINE_LOCATION")
            executeShellCommand("pm revoke $packageName android.permission.ACCESS_COARSE_LOCATION")
        }

        uiRobot = UiRobot(composeRule)
        permissionRobot = PermissionRobot(composeRule)
        systemPermissionRobot = SystemPermissionRobot()
    }

    @Test
    fun `app launches without crash`() {
        composeRule.onRoot().assertExists()
    }

    @Test
    fun `UI_DIALOG_S1 - should show system dialog after education dialog`() {
        permissionRobot
            .tapCardToStartPermissionFlow(EMPTY_CARD)
        uiRobot
            .assertAppDialogPresented(EDUCATION_DIALOG)
            .confirmAppDialog(EDUCATION_DIALOG)

        systemPermissionRobot
            .assertSystemDialogAppeared(composeRule)
            .allowWhileUsingApp(composeRule)
    }

    @Test
    fun `UI_DIALOG_S2 - should show rationale dialog when system permission denied`() {
        permissionRobot
            .tapCardToStartPermissionFlow(EMPTY_CARD)
        uiRobot
            .assertAppDialogPresented(EDUCATION_DIALOG)
            .confirmAppDialog(EDUCATION_DIALOG)
        systemPermissionRobot
            .assertSystemDialogAppeared(composeRule)
            .denyPermission(composeRule)
        uiRobot
            .assertAppDialogPresented(RATIONALE_DIALOG)
            .confirmAppDialog(RATIONALE_DIALOG)
    }

    // UI_DIALOG_S3 - should show rationale dialog after system dialog denial 🖐️

    // UI_DIALOG_S4 - should show GPS card after allowing via rationale ✅ → see UI_CARD_S2_1

    @Test
    fun `UI_DIALOG_S5 - should show permanently denied dialog after denying twice`() {
        permissionRobot
            .updateFakePermissionRepository(LocationPermission.DeniedPermanently)
            .tapCardToStartPermissionFlow(EMPTY_CARD)
        uiRobot
            .assertAppDialogPresented(DENIED_PERMANENTLY_DIALOG)
    }

    @Test
    fun `UI_DIALOG_S6 - should show system dialog when tapping outdated GPS card with denied permission`() {
        permissionRobot
            .updateFakePermissionRepository(LocationPermission.Denied)
        uiRobot
            .addShabbatCard(SavedLocation.GPS_ID, "My gsp city")
            .assertCardPresented(GPS_CARD)
        permissionRobot
            .tapCardToStartPermissionFlow(GPS_CARD)
        systemPermissionRobot
            .assertSystemDialogAppeared(composeRule)
            .allowWhileUsingApp(composeRule)
    }

    @Test
    fun `UI_DIALOG_S7 - should show open settings dialog when tapping outdated GPS card with permanently denied permission`() {
        permissionRobot
            .updateFakePermissionRepository(LocationPermission.DeniedPermanently)
        uiRobot
            .addShabbatCard(SavedLocation.GPS_ID, "My gsp city")
        permissionRobot
            .tapCardToStartPermissionFlow(GPS_CARD)
        uiRobot
            .assertAppDialogPresented(DENIED_PERMANENTLY_DIALOG)

        composeRule.onNodeWithTag(BUTTON_DIALOG_CONFIRM).assertExists()
        composeRule.onNodeWithTag(BUTTON_DIALOG_DISMISS).assertExists()
    }

    @Test
    fun `UI_DIALOG_S8 - should show Education dialog when card tapped with Idle permission`() {
        permissionRobot
            .updateFakePermissionRepository(LocationPermission.Idle)
            .tapCardToStartPermissionFlow(EMPTY_CARD)
        uiRobot
            .assertAppDialogPresented(EDUCATION_DIALOG)
    }

    @Test
    fun `UI_DIALOG_S9 - should keep empty card when Education dialog is dismissed`() {
        permissionRobot
            .updateFakePermissionRepository(LocationPermission.Idle)
            .tapCardToStartPermissionFlow(EMPTY_CARD)
        uiRobot
            .assertAppDialogPresented(EDUCATION_DIALOG)
            .dismissAppDialog(EDUCATION_DIALOG)
            .assertCardPresented(EMPTY_CARD)
            .assertCardNotPresented(GPS_CARD)
            .assertCardNotPresented(LOCATION_CARD)
    }
}
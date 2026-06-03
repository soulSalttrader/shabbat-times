package il.soulSalttrader.shabbattimes.permission

import dagger.hilt.android.testing.HiltAndroidTest
import il.soulSalttrader.shabbattimes.BaseInstrumentedTest
import il.soulSalttrader.shabbattimes.PermissionRobot
import il.soulSalttrader.shabbattimes.TestTags.EDUCATION_DIALOG
import il.soulSalttrader.shabbattimes.TestTags.EMPTY_CARD
import il.soulSalttrader.shabbattimes.TestTags.GPS_CARD
import org.junit.Test

@HiltAndroidTest
class SystemDialogFlowTest : BaseInstrumentedTest() {

    @Test
    fun `UI_CARD_S2_1 - should show GPS card when permission granted `() {
        PermissionRobot(composeRule)
            .tapCardToStartFlow(EMPTY_CARD)
            .assertAppDialogPresented(EDUCATION_DIALOG)
            .confirmAppDialog(EDUCATION_DIALOG)
            .waitForSystemPermissionDialog()
            .grantSystemPermission()
            .waitForShabbatCard(GPS_CARD)
            .assertShabbatCardPresented(GPS_CARD)
    }
}
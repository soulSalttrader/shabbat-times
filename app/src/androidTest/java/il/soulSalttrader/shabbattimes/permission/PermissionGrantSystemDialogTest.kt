package il.soulSalttrader.shabbattimes.permission

import dagger.hilt.android.testing.HiltAndroidTest
import il.soulSalttrader.shabbattimes.BaseInstrumentedTest
import il.soulSalttrader.shabbattimes.PermissionRobot
import org.junit.Test

@HiltAndroidTest
class PermissionGrantSystemDialogTest : BaseInstrumentedTest() {

    @Test
    fun `UI_PERM_FRESH_S1 - GPS card appears after granting permission`() {
        PermissionRobot(composeRule)
            .tapEmptyCardToStartFlow()
            .assertEducationDialogVisible()
            .confirmEducationDialog()
            .waitForSystemPermissionDialog()
            .grantSystemPermission()
            .waitForGpsCard()
            .assertGpsCardVisible()
    }
}
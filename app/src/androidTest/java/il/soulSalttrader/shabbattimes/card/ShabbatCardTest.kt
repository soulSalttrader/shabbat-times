package il.soulSalttrader.shabbattimes.card

import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidTest
import il.soulSalttrader.shabbattimes.BaseInstrumentedTest
import il.soulSalttrader.shabbattimes.ShabbatCardRobot
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
    fun `should not show drag handle on Empty card`() {
        ShabbatCardRobot(composeRule)
            .assertEmptyCardVisible()
            .assertNoDragHandleOnEmptyCard()
    }

    @Test
    fun `should show Empty card when no locations are saved`() {
        ShabbatCardRobot(composeRule)
            .assertEmptyCardVisible()
    }

    @Test
    fun `should show drag handle on GPS card`() {
        ShabbatCardRobot(composeRule)
            .addGPSShabbatCard()
            .assertDragHandleOnGpsCard()
    }

    @Test
    fun `should show drag handle on location card`() {
        ShabbatCardRobot(composeRule)
            .addLocationShabbatCard()
            .assertDragHandleOnLocationCard()
    }
}
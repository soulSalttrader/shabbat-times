package il.soulSalttrader.shabbattimes

import android.util.Log
import androidx.compose.ui.test.junit4.v2.createEmptyComposeRule
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import il.soulSalttrader.shabbattimes.di.FakePermissionRepositoryModule
import il.soulSalttrader.shabbattimes.di.FakePersistenceModule
import il.soulSalttrader.shabbattimes.model.LocationPermission
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
abstract class BaseInstrumentedTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createEmptyComposeRule()

    lateinit var activityScenario: ActivityScenario<MainActivity>

    open fun setupTest() {}

    @Before
    fun setup() {
        hiltRule.inject()
        resetFakes()

        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        activityScenario.moveToState(Lifecycle.State.RESUMED)

        setupTest()
        composeRule.waitForIdle()
    }

    @After
    fun tearDown() {
        runCatching {
            repeat(3) {
                runCatching { Espresso.pressBack() }
                composeRule.waitForIdle()
            }

            activityScenario.close()

            resetFakes()
        }.onFailure { e ->  Log.w("BaseInstrumentedTest", "Cleanup failed", e) }
    }

    private fun resetFakes() {
        runBlocking {
            FakePersistenceModule.fakeSavedLocations.clear()
            FakePersistenceModule.fakeCurrentLocation.update(null)
        }
        FakePermissionRepositoryModule.fakePermissionRepository
            .updatePermissionState(LocationPermission.Idle)
    }
}
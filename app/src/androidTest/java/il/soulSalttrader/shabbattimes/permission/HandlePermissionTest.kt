package il.soulSalttrader.shabbattimes.permission

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent
import il.soulSalttrader.shabbattimes.ui.permission.HandlePermissions
import il.soulSalttrader.shabbattimes.ui.permission.LocalPermissionHandler
import il.soulSalttrader.shabbattimes.ui.permission.PermissionUiState
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HandlePermissionsTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun `PERM_DISPATCH_S1_1 - should dispatch AllGranted exactly once`() {
        val dispatchedEvents = mutableListOf<PermissionEvent>()

        composeRule.setContent {
            CompositionLocalProvider(
                LocalPermissionHandler provides FakePermissionHandler(granted = true)
            ) {
                HandlePermissions(
                    permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    permissionState = PermissionUiState(permission = PermissionState.Requesting),
                    dispatch = { dispatchedEvents.add(it) }
                )
            }
        }

        composeRule.waitForIdle()
        assert(dispatchedEvents.count { it == PermissionEvent.AllGranted } == 1)
    }

    @Test
    fun `PERM_DISPATCH_S1_2 - should dispatch DeniedWithRationale exactly once`() {
        val dispatchedEvents = mutableListOf<PermissionEvent>()

        composeRule.setContent {
            CompositionLocalProvider(
                LocalPermissionHandler provides FakePermissionHandler(
                    granted = false,
                    requestResult = PermissionResult.Explain(listOf(Manifest.permission.ACCESS_FINE_LOCATION)),
                )
            ) {
                HandlePermissions(
                    permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    permissionState = PermissionUiState(permission = PermissionState.Requesting),
                    dispatch = { dispatchedEvents.add(it) }
                )
            }
        }

        composeRule.waitForIdle()
        assert(dispatchedEvents.count { it == PermissionEvent.DeniedWithRationale } == 1)
    }
}
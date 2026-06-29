package il.soulSalttrader.shabbattimes.permission

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.matcher.ViewMatchers.assertThat
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
                    dispatch = { dispatchedEvents.add(it) },
                    returnedFromSettings = false,
                    onSettingsHandled = {},
                )
            }
        }

        composeRule.waitForIdle()
        assert(dispatchedEvents.count { it == PermissionEvent.SystemGranted } == 1)
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
                    dispatch = { dispatchedEvents.add(it) },
                    returnedFromSettings = false,
                    onSettingsHandled = {},
                )
            }
        }

        composeRule.waitForIdle()
        assert(dispatchedEvents.count { it == PermissionEvent.SystemDenied } == 1)
    }

    @Test
    fun `PERM_DISPATCH_S1_3 - should call onSettingsHandled when returnedFromSettings is true`() {
        var handled = 0

        composeRule.setContent {
            CompositionLocalProvider(
                LocalPermissionHandler provides FakePermissionHandler(granted = true)
            ) {
                HandlePermissions(
                    permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    permissionState = PermissionUiState(permission = PermissionState.Idle),
                    dispatch = {},
                    returnedFromSettings = true,
                    onSettingsHandled = { handled++ },
                )
            }
        }

        composeRule.waitForIdle()
        assert(handled == 1)
    }

    @Test
    fun `PERM_DISPATCH_S1_4 - should not call onSettingsHandled when returnedFromSettings is false`() {
        var handled = 0

        composeRule.setContent {
            CompositionLocalProvider(
                LocalPermissionHandler provides FakePermissionHandler(granted = true)
            ) {
                HandlePermissions(
                    permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    permissionState = PermissionUiState(permission = PermissionState.Idle),
                    dispatch = {},
                    returnedFromSettings = true,
                    onSettingsHandled = { handled++ },
                )
            }
        }

        composeRule.waitForIdle()
        assert(handled == 1)
    }

    @Test
    fun `PERM_DISPATCH_S1_4 - should dispatch event only once even if ON_RESUME fires multiple times`() {
        val events = mutableListOf<PermissionEvent>()
        var returnedFromSettings by mutableStateOf(true)

        composeRule.setContent {
            CompositionLocalProvider(LocalPermissionHandler provides FakePermissionHandler(granted = true)) {
                HandlePermissions(
                    permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    permissionState = PermissionUiState(permission = PermissionState.Idle),
                    returnedFromSettings = returnedFromSettings,
                    onSettingsHandled = { returnedFromSettings = false },
                    dispatch = { events.add(it) },
                )
            }
        }

        composeRule.activityRule.scenario.moveToState(Lifecycle.State.STARTED)
        composeRule.activityRule.scenario.moveToState(Lifecycle.State.RESUMED)
        composeRule.waitForIdle()

        assert(events.size == 1)
    }
}
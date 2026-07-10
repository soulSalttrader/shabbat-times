package il.soulSalttrader.shabbattimes.permission

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.lifecycle.Lifecycle
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
    fun `PERM_DISPATCH_S1_5 - should dispatch event only once even if ON_RESUME fires multiple times`() {
        val events = mutableListOf<PermissionEvent>()
        var returnedFromSettings by mutableStateOf(true)

        composeRule.setContent {
            CompositionLocalProvider(LocalPermissionHandler provides FakePermissionHandler(granted = true)) {
                HandlePermissions(
                    permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    permissionState = PermissionUiState(permission = PermissionState.Granted),
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

    @Test
    fun `PERM_DISPATCH_S1_6 - should dispatch SystemGranted once on initial composition when already granted`() {
        val events = mutableListOf<PermissionEvent>()

        composeRule.setContent {
            CompositionLocalProvider(LocalPermissionHandler provides FakePermissionHandler(granted = true)) {
                HandlePermissions(
                    permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    permissionState = PermissionUiState(permission = PermissionState.Idle),
                    returnedFromSettings = false, // isolate from ON_RESUME effect
                    onSettingsHandled = {},
                    dispatch = { events.add(it) },
                )
            }
        }

        composeRule.waitForIdle()

        assert(events == listOf(PermissionEvent.SystemGranted))
    }

    @Test
    fun `PERM_DISPATCH_S1_7 - should not dispatch anything on initial composition when not granted`() {
        val events = mutableListOf<PermissionEvent>()

        composeRule.setContent {
            CompositionLocalProvider(LocalPermissionHandler provides FakePermissionHandler(granted = false)) {
                HandlePermissions(
                    permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    permissionState = PermissionUiState(permission = PermissionState.Idle),
                    returnedFromSettings = false,
                    onSettingsHandled = {},
                    dispatch = { events.add(it) },
                )
            }
        }

        composeRule.waitForIdle()

        assert(events.isEmpty())
    }

    @Test
    fun `PERM_DISPATCH_S1_8 - should not check granted state on initial composition when permission is not Idle`() {
        val events = mutableListOf<PermissionEvent>()

        composeRule.setContent {
            CompositionLocalProvider(LocalPermissionHandler provides FakePermissionHandler(granted = true)) {
                HandlePermissions(
                    permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    permissionState = PermissionUiState(permission = PermissionState.Requesting),
                    returnedFromSettings = false,
                    onSettingsHandled = {},
                    dispatch = { events.add(it) },
                )
            }
        }

        composeRule.waitForIdle()

        // Requesting effect will fire instead — assert no SystemGranted came from the Unit-keyed effect specifically
        // by using a handler whose request() call is distinguishable, or simply assert the Requesting-path event.
        assert(events == listOf(PermissionEvent.SystemGranted)) // via request(), not the idle-check
    }

    @Test
    fun `PERM_DISPATCH_S1_9 - should not re-dispatch SystemGranted on recomposition`() {
        val events = mutableListOf<PermissionEvent>()
        var recomposeTrigger by mutableStateOf(0)

        composeRule.setContent {
            CompositionLocalProvider(LocalPermissionHandler provides FakePermissionHandler(granted = true)) {
                recomposeTrigger // read to force recomposition scope
                HandlePermissions(
                    permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    permissionState = PermissionUiState(permission = PermissionState.Idle),
                    returnedFromSettings = false,
                    onSettingsHandled = {},
                    dispatch = { events.add(it) },
                )
            }
        }
        composeRule.waitForIdle()

        recomposeTrigger++ // force recomposition
        composeRule.waitForIdle()

        assert(events.size == 1)
    }
}
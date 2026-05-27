package il.soulSalttrader.shabbattimes.ui.reducer

import il.soulSalttrader.shabbattimes.model.LocationPermission
import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent
import il.soulSalttrader.shabbattimes.ui.permission.PermissionUiState
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class PermissionReducerTest : DescribeSpec({

    val idle = PermissionUiState()

    describe("PERM_FRESH - fresh install flow") {

        // PERM_FRESH_S1 — Grant on first ask
        it("PERM_FRESH_S1 - ShowEducation sets Education + shows dialog") {
            val result = PermissionEvent.ShowEducation.reducer reduce idle
            result.permission shouldBe PermissionState.Education
            result.isDialogVisible shouldBe true
        }

        it("PERM_FRESH_S1 - Request sets Requesting + shows dialog") {
            val result = PermissionEvent.Request.reducer reduce idle
            result.permission shouldBe PermissionState.Requesting
            result.isDialogVisible shouldBe true
        }

        it("PERM_FRESH_S1 - AllGranted sets Granted") {
            val result = PermissionEvent.AllGranted.reducer reduce idle
            result.permission shouldBe PermissionState.Granted
        }

        // PERM_FRESH_S2 — Deny on first ask
        it("PERM_FRESH_S2 - DeniedWithRationale sets Denied") {
            val result = PermissionEvent.DeniedWithRationale.reducer reduce idle
            result.permission shouldBe PermissionState.Denied
        }

        it("PERM_FRESH_S2 - DismissedRationale hides dialog but keeps Denied state") {
            val denied = idle.copy(permission = PermissionState.Denied, isDialogVisible = true)
            val result = PermissionEvent.DismissedRationale.reducer reduce denied
            result.isDialogVisible shouldBe false
            result.permission shouldBe PermissionState.Denied
        }

        // PERM_FRESH_S3 — Deny then allow via rationale
        it("PERM_FRESH_S3 - AcceptedRationale sets Requesting") {
            val denied = idle.copy(permission = PermissionState.Denied)
            val result = PermissionEvent.AcceptedRationale.reducer reduce denied
            result.permission shouldBe PermissionState.Requesting
        }

        // PERM_FRESH_S4 — Deny permanently
        it("PERM_FRESH_S4 - DeniedPermanently sets DeniedPermanently") {
            val result = PermissionEvent.DeniedPermanently.reducer reduce idle
            result.permission shouldBe PermissionState.DeniedPermanently
        }

        // PERM_FRESH_S5 — Dismiss system permission dialog
        it("PERM_FRESH_S5 - System dialog dismiss keeps Requesting state") {
            val requesting = idle.copy(permission = PermissionState.Requesting)
            requesting.permission shouldBe PermissionState.Requesting
            requesting.isDialogVisible shouldBe false
        }
    }

    describe("PERM_SETTINGS - app settings flow") {

        val permanentlyDenied = idle.copy(permission = PermissionState.DeniedPermanently)

        // PERM_SETTINGS_S1 - Grant permission in Settings
        it("PERM_SETTINGS_S1 - ShowDeniedPermanentlyDialog sets DeniedPermanently + shows dialog") {
            val result = PermissionEvent.ShowDeniedPermanentlyDialog.reducer reduce idle
            result.permission shouldBe PermissionState.DeniedPermanently
            result.isDialogVisible shouldBe true
        }

        it("PERM_SETTINGS_S1 - ReturnedFromAppSettings resets DeniedPermanently to Idle") {
            val result = PermissionEvent.ReturnedFromAppSettings.reducer reduce permanentlyDenied
            result.permission shouldBe PermissionState.Idle
            result.isDialogVisible shouldBe false
        }

        it("PERM_SETTINGS_S1 - ReturnedFromAppSettings does not trigger auto-request") {
            val result = PermissionEvent.ReturnedFromAppSettings.reducer reduce permanentlyDenied
            result.permission shouldBe PermissionState.Idle
            result.isDialogVisible shouldBe false
        }

        it("after returning from Settings, tapping card starts fresh flow") {
            val afterReturn = PermissionEvent.ReturnedFromAppSettings.reducer reduce permanentlyDenied
            afterReturn.permission shouldBe PermissionState.Idle

            val afterTap = PermissionEvent.ShowEducation.reducer reduce afterReturn
            afterTap.permission shouldBe PermissionState.Education
        }

        // PERM_SETTINGS_S2 - Set to "Ask every time" in Settings
        it("PERM_SETTINGS_S2 - ReturnedFromAppSettings works from any state") {
            val result = PermissionEvent.ReturnedFromAppSettings.reducer reduce idle
            result.permission shouldBe PermissionState.Idle
        }

        // PERM_SETTINGS_S3 - Return from Settings without change
        it("PERM_SETTINGS_S3 - No change in Settings keeps DeniedPermanently") {
            val result = PermissionEvent.RequestedAppSettings.reducer reduce permanentlyDenied
            result.permission shouldBe PermissionState.DeniedPermanently
        }
    }

    describe("PERM_EDGE - reducer edge cases") {

        // PERM_EDGE_S6 - Unrelated event should not touch permission
        it("PERM_EDGE_S4 - permission state unaffected by unrelated events") {
            val granted = idle.copy(permission = PermissionState.Granted)
            // dispatching an unrelated event should not touch permission
            granted.permission shouldBe PermissionState.Granted
        }

        it("AllGranted from Denied sets Granted") {
            val denied = idle.copy(permission = PermissionState.Denied)
            val result = PermissionEvent.AllGranted.reducer reduce denied
            result.permission shouldBe PermissionState.Granted
        }

        it("AllGranted from DeniedPermanently sets Granted") {
            val permDenied = idle.copy(permission = PermissionState.DeniedPermanently)
            val result = PermissionEvent.AllGranted.reducer reduce permDenied
            result.permission shouldBe PermissionState.Granted
        }
    }

    describe("PERM_MAPPING - PermissionChanged mapping") {

        it("PERM_MAPPING_S1 - maps Idle → Idle") {
            val result = PermissionEvent.PermissionChanged(LocationPermission.Idle).reducer reduce idle
            result.permission shouldBe PermissionState.Idle
        }

        it("PERM_MAPPING_S2 - maps Education → Education") {
            val result = PermissionEvent.PermissionChanged(LocationPermission.Education).reducer reduce idle
            result.permission shouldBe PermissionState.Education
        }

        it("PERM_MAPPING_S3 - maps Requesting → Requesting") {
            val result = PermissionEvent.PermissionChanged(LocationPermission.Requesting).reducer reduce idle
            result.permission shouldBe PermissionState.Requesting
        }

        it("PERM_MAPPING_S4 - maps Granted → Granted") {
            val result = PermissionEvent.PermissionChanged(LocationPermission.Granted).reducer reduce idle
            result.permission shouldBe PermissionState.Granted
        }

        it("PERM_MAPPING_S5 - maps Denied → Denied") {
            val result = PermissionEvent.PermissionChanged(LocationPermission.Denied).reducer reduce idle
            result.permission shouldBe PermissionState.Denied
        }

        it("PERM_MAPPING_S6 - maps DeniedPermanently → DeniedPermanently") {
            val result = PermissionEvent.PermissionChanged(LocationPermission.DeniedPermanently).reducer reduce idle
            result.permission shouldBe PermissionState.DeniedPermanently
        }
    }
})
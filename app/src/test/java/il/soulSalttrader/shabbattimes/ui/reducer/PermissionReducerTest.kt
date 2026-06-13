package il.soulSalttrader.shabbattimes.ui.reducer

import il.soulSalttrader.shabbattimes.model.LocationPermission
import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent
import il.soulSalttrader.shabbattimes.ui.permission.PermissionUiState
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class PermissionReducerTest : DescribeSpec({

    val idle = PermissionUiState()

    describe("PERM_FRESH_S1 - SCENARIO: User grants permission on first ask") {
        it("PERM_FRESH_S1_REDUCER_1 - should set Education and show dialog on ShowEducation") {
            val result = PermissionEvent.ShowEducation.reducer reduce idle
            result.permission shouldBe PermissionState.Education
            result.isDialogVisible shouldBe true
        }

        it("PERM_FRESH_S1_REDUCER_2 - should set Requesting and show dialog on Request") {
            val result = PermissionEvent.Request.reducer reduce idle
            result.permission shouldBe PermissionState.Requesting
            result.isDialogVisible shouldBe true
        }

        it("PERM_FRESH_S1_REDUCER_3 - should set Granted on AllGranted") {
            val result = PermissionEvent.AllGranted.reducer reduce idle
            result.permission shouldBe PermissionState.Granted
        }
    }

    describe("PERM_FRESH_S2 - SCENARIO: User denies permission on first ask") {
        it("PERM_FRESH_S2_REDUCER_1 - should set Denied on DeniedWithRationale") {
            val result = PermissionEvent.DeniedWithRationale.reducer reduce idle
            result.permission shouldBe PermissionState.Denied
        }

        it("PERM_FRESH_S2_REDUCER_2 - should hide dialog and keep Denied on DismissedRationale") {
            val denied = idle.copy(permission = PermissionState.Denied, isDialogVisible = true)
            val result = PermissionEvent.DismissedRationale.reducer reduce denied
            result.isDialogVisible shouldBe false
            result.permission shouldBe PermissionState.Denied
        }
    }

    describe("PERM_FRESH_S3 - SCENARIO: User denies then allows via rationale") {
        it("PERM_FRESH_S3_REDUCER_1 - should set Requesting on AcceptedRationale") {
            val denied = idle.copy(permission = PermissionState.Denied)
            val result = PermissionEvent.AcceptedRationale.reducer reduce denied
            result.permission shouldBe PermissionState.Requesting
        }
    }

    describe("PERM_FRESH_S4 - SCENARIO: User permanently denies permission") {
        it("PERM_FRESH_S4_REDUCER_1 - should set DeniedPermanently on DeniedPermanently") {
            val result = PermissionEvent.DeniedPermanently.reducer reduce idle
            result.permission shouldBe PermissionState.DeniedPermanently
        }
    }

    describe("PERM_FRESH_S5 - SCENARIO: User dismisses system dialog without choosing") {
        it("PERM_FRESH_S5_REDUCER_1 - should keep Requesting state when system dialog dismissed") {
            val requesting = idle.copy(permission = PermissionState.Requesting)
            requesting.permission shouldBe PermissionState.Requesting
            requesting.isDialogVisible shouldBe false
        }
    }

    describe("PERM_SETTINGS_S1 - SCENARIO: User grants permission in settings") {
        val permanentlyDenied = idle.copy(permission = PermissionState.DeniedPermanently)

        it("PERM_SETTINGS_S1_REDUCER_1 - should set DeniedPermanently and show dialog on ShowDeniedPermanentlyDialog") {
            val result = PermissionEvent.ShowDeniedPermanentlyDialog.reducer reduce idle
            result.permission shouldBe PermissionState.DeniedPermanently
            result.isDialogVisible shouldBe true
        }

        it("PERM_SETTINGS_S1_REDUCER_2 - should reset to Idle on ReturnedFromAppSettings") {
            val result = PermissionEvent.ReturnedFromAppSettings.reducer reduce permanentlyDenied
            result.permission shouldBe PermissionState.Idle
            result.isDialogVisible shouldBe false
        }

        it("PERM_SETTINGS_S1_REDUCER_3 - should start fresh Education flow after returning from settings") {
            val afterReturn =
                PermissionEvent.ReturnedFromAppSettings.reducer reduce permanentlyDenied
            afterReturn.permission shouldBe PermissionState.Idle

            val afterTap = PermissionEvent.ShowEducation.reducer reduce afterReturn
            afterTap.permission shouldBe PermissionState.Education
        }
    }

    describe("PERM_SETTINGS_S2 - SCENARIO: User sets Ask every time in settings") {
        val state = idle.copy(permission = PermissionState.Denied)

        it("PERM_SETTINGS_S2_REDUCER_1 - should reset to Idle from any state on ReturnedFromAppSettings") {
            val result = PermissionEvent.ReturnedFromAppSettings.reducer reduce state
            result.permission shouldBe PermissionState.Idle
        }
    }

    describe("PERM_SETTINGS_S3 - SCENARIO: User ignores settings and returns") {
        val permanentlyDenied = idle.copy(permission = PermissionState.DeniedPermanently)

        it("PERM_SETTINGS_S3_REDUCER_1 - should keep DeniedPermanently on RequestedAppSettings") {
            val result = PermissionEvent.RequestedAppSettings.reducer reduce permanentlyDenied
            result.permission shouldBe PermissionState.DeniedPermanently
        }
    }

    describe("PERM_EDGE_S6 - SCENARIO: Unrelated event should not touch permission") {
        it("PERM_EDGE_S6_REDUCER_1 - should not change Granted state on unrelated event") {
            val granted = idle.copy(permission = PermissionState.Granted)
            // DismissedRationale is unrelated to Granted state
            val result = PermissionEvent.DismissedRationale.reducer reduce granted
            result.permission shouldBe PermissionState.Granted
        }
    }

    describe("PERM_EDGE_S7 - SCENARIO: AllGranted works from any denied state") {
        it("PERM_EDGE_S7_REDUCER_1 - should set Granted from Denied on AllGranted ") {
            val denied = idle.copy(permission = PermissionState.Denied)
            val result = PermissionEvent.AllGranted.reducer reduce denied
            result.permission shouldBe PermissionState.Granted
        }

        it("PERM_EDGE_S7_REDUCER_2 - should set Granted from DeniedPermanently on AllGranted") {
            val permDenied = idle.copy(permission = PermissionState.DeniedPermanently)
            val result = PermissionEvent.AllGranted.reducer reduce permDenied
            result.permission shouldBe PermissionState.Granted
        }
    }

    describe("PERM_MAPPING - SCENARIO: LocationPermission maps correctly to PermissionState") {
        it("PERM_MAPPING_S1 - should map Idle to PermissionState.Idle") {
            val result = PermissionEvent.PermissionChanged(LocationPermission.Idle).reducer reduce idle
            result.permission shouldBe PermissionState.Idle
        }

        it("PERM_MAPPING_S2 - should map Education to PermissionState.Education") {
            val result = PermissionEvent.PermissionChanged(LocationPermission.Education).reducer reduce idle
            result.permission shouldBe PermissionState.Education
        }

        it("PERM_MAPPING_S3 - should map Requesting to PermissionState.Requesting") {
            val result = PermissionEvent.PermissionChanged(LocationPermission.Requesting).reducer reduce idle
            result.permission shouldBe PermissionState.Requesting
        }

        it("PERM_MAPPING_S4 - should map Granted to PermissionState.Granted") {
            val result = PermissionEvent.PermissionChanged(LocationPermission.Granted).reducer reduce idle
            result.permission shouldBe PermissionState.Granted
        }

        it("PERM_MAPPING_S5 - should map Denied to PermissionState.Denied") {
            val result = PermissionEvent.PermissionChanged(LocationPermission.Denied).reducer reduce idle
            result.permission shouldBe PermissionState.Denied
        }

        it("PERM_MAPPING_S6 - should map DeniedPermanently to PermissionState.DeniedPermanently") {
            val result = PermissionEvent.PermissionChanged(LocationPermission.DeniedPermanently).reducer reduce idle
            result.permission shouldBe PermissionState.DeniedPermanently
        }
    }
})
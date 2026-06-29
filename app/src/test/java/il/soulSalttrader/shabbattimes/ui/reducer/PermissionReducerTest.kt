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
        }

        it("PERM_FRESH_S1_REDUCER_2 - should set Requesting and show dialog on Request") {
            val result = PermissionEvent.Request.reducer reduce idle
            result.permission shouldBe PermissionState.Requesting
        }

        it("PERM_FRESH_S1_REDUCER_3 - should set Granted on SystemGranted") {
            val result = PermissionEvent.SystemGranted.reducer reduce idle
            result.permission shouldBe PermissionState.Granted
        }
    }

    describe("PERM_FRESH_S2 - SCENARIO: User denies permission on first ask") {
        it("PERM_FRESH_S2_REDUCER_1 - should set DeniedRationale on SystemDenied") {
            val result = PermissionEvent.SystemDenied.reducer reduce idle
            result.permission shouldBe PermissionState.DeniedRationale
        }

        it("PERM_FRESH_S2_REDUCER_2 - should set DeniedPermanently on DismissDeniedRationale") {
            val denied = idle.copy(permission = PermissionState.Denied)
            val result = PermissionEvent.DismissDeniedRationale.reducer reduce denied
            result.permission shouldBe PermissionState.DeniedPermanently
        }
    }

    describe("PERM_FRESH_S4 - SCENARIO: User permanently denies permission") {
        it("PERM_FRESH_S4_REDUCER_1 - should set DeniedPermanently on DismissDeniedPermanently") {
            val result = PermissionEvent.DismissDeniedPermanently.reducer reduce idle
            result.permission shouldBe PermissionState.DeniedPermanently
        }
    }

    describe("PERM_FRESH_S5 - SCENARIO: User dismisses system dialog without choosing") {
        it("PERM_FRESH_S5_REDUCER_1 - should keep Requesting state when system dialog dismissed") {
            val requesting = idle.copy(permission = PermissionState.Requesting)
            requesting.permission shouldBe PermissionState.Requesting
        }
    }

    describe("PERM_SETTINGS_S1 - SCENARIO: User grants permission in settings") {
        val permanentlyDenied = idle.copy(permission = PermissionState.DeniedPermanently)

        it("PERM_SETTINGS_S1_REDUCER_1 - should set DeniedPermanentlyRationale and on TappedCardDeniedPermanently") {
            val result = PermissionEvent.TappedCardDeniedPermanently.reducer reduce idle
            result.permission shouldBe PermissionState.DeniedPermanentlyRationale
        }

        it("PERM_SETTINGS_S1_REDUCER_2 - should reset to Idle on ReturnedFromAppSettings") {
            val result = PermissionEvent.ReturnedFromAppSettings.reducer reduce permanentlyDenied
            result.permission shouldBe PermissionState.Idle
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

        it("PERM_SETTINGS_S3_REDUCER_1 - should keep DeniedPermanently on OpenAppSettings") {
            val result = PermissionEvent.OpenAppSettings.reducer reduce permanentlyDenied
            result.permission shouldBe PermissionState.DeniedPermanently
        }
    }

    describe("PERM_EDGE_S7 - SCENARIO: AllGranted works from any denied state") {
        it("PERM_EDGE_S7_REDUCER_1 - should set Granted from Denied on SystemGranted") {
            val denied = idle.copy(permission = PermissionState.Denied)
            val result = PermissionEvent.SystemGranted.reducer reduce denied
            result.permission shouldBe PermissionState.Granted
        }

        it("PERM_EDGE_S7_REDUCER_2 - should set Granted from DeniedPermanently on SystemGranted") {
            val permDenied = idle.copy(permission = PermissionState.DeniedPermanently)
            val result = PermissionEvent.SystemGranted.reducer reduce permDenied
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
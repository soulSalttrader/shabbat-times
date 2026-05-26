package il.soulSalttrader.shabbattimes.ui.reducer

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
})
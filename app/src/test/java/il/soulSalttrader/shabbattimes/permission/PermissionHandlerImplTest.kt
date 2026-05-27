package il.soulSalttrader.shabbattimes.permission

import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class PermissionHandlerImplTest : DescribeSpec({

    val fine   = "android.permission.ACCESS_FINE_LOCATION"
    val coarse = "android.permission.ACCESS_COARSE_LOCATION"

    fun makeHandler(
        granted: Set<String> = emptySet(),
        rationale: Set<String> = emptySet(),
    ) = FakePermissionHandler(granted, rationale)

    describe("resolvePermissionEvent") {

        // PERM_FRESH_S1
        it("all granted → AllGranted") {
            makeHandler(granted = setOf(fine, coarse))
                .resolvePermissionEvent(listOf(fine, coarse)) shouldBe PermissionEvent.AllGranted
        }

        // PERM_FRESH_S2
        it("denied + shouldShowRationale → DeniedWithRationale") {
            makeHandler(rationale = setOf(fine, coarse))
                .resolvePermissionEvent(listOf(fine, coarse)) shouldBe PermissionEvent.DeniedWithRationale
        }

        // PERM_FRESH_S4
        it("denied + no rationale → null (permanent denial indistinguishable from fresh install)") {
            makeHandler()
                .resolvePermissionEvent(listOf(fine, coarse)) shouldBe null
        }

        it("partial grant — one granted, one with rationale → DeniedWithRationale") {
            makeHandler(granted = setOf(coarse), rationale = setOf(fine))
                .resolvePermissionEvent(listOf(fine, coarse)) shouldBe PermissionEvent.DeniedWithRationale
        }

        it("partial grant — one granted, one with no rationale → null") {
            makeHandler(granted = setOf(coarse))
                .resolvePermissionEvent(listOf(fine, coarse)) shouldBe null
        }

        it("mixed rationale — any permission with rationale → DeniedWithRationale") {
            makeHandler(rationale = setOf(coarse))
                .resolvePermissionEvent(listOf(fine, coarse)) shouldBe PermissionEvent.DeniedWithRationale
        }
    }
})
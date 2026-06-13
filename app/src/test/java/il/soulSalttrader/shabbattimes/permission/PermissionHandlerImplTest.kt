package il.soulSalttrader.shabbattimes.permission

import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class PermissionHandlerImplTest : DescribeSpec({

    val fine = "android.permission.ACCESS_FINE_LOCATION"
    val coarse = "android.permission.ACCESS_COARSE_LOCATION"

    fun makeHandler(
        granted: Set<String> = emptySet(),
        rationale: Set<String> = emptySet(),
    ) = FakePermissionHandler(granted, rationale)

    describe("PERM_FRESH_S1 - SCENARIO: User grants permission on first ask") {
        it("PERM_HANDLER_S1 - should return AllGranted when all permissions granted") {
            makeHandler(granted = setOf(fine, coarse))
                .resolvePermissionEvent(listOf(fine, coarse)) shouldBe PermissionEvent.AllGranted
        }
    }

    describe("PERM_FRESH_S2 - SCENARIO: User denies permission on first ask") {
        it("PERM_HANDLER_S2 - should return DeniedWithRationale when all denied with rationale") {
            makeHandler(rationale = setOf(fine, coarse))
                .resolvePermissionEvent(listOf(fine, coarse)) shouldBe PermissionEvent.DeniedWithRationale
        }
    }

    describe("PERM_FRESH_S4 - SCENARIO: User permanently denies permission") {
        it("PERM_HANDLER_S3 - should return null when all denied without rationale") {
            makeHandler()
                .resolvePermissionEvent(listOf(fine, coarse)) shouldBe null
        }
    }

    describe("PERM_HANDLER_PARTIAL - SCENARIO: Permission resolution handles partial grants correctly") {
        it("PERM_HANDLER_S4 - should return DeniedWithRationale when partially granted with rationale") {
            makeHandler(granted = setOf(coarse), rationale = setOf(fine))
                .resolvePermissionEvent(listOf(fine, coarse)) shouldBe PermissionEvent.DeniedWithRationale
        }

        it("PERM_HANDLER_S5 - should return null when partially granted without rationale") {
            makeHandler(granted = setOf(coarse))
                .resolvePermissionEvent(listOf(fine, coarse)) shouldBe null
        }

        it("PERM_HANDLER_S6 - should return DeniedWithRationale when any permission has rationale") {
            makeHandler(rationale = setOf(coarse))
                .resolvePermissionEvent(listOf(fine, coarse)) shouldBe PermissionEvent.DeniedWithRationale
        }
    }
})
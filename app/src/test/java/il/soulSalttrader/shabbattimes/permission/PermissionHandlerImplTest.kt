package il.soulSalttrader.shabbattimes.permission

import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class PermissionHandlerTest : DescribeSpec({

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
    }
})
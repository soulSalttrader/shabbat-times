package il.soulSalttrader.shabbattimes.ui.search

import il.soulSalttrader.shabbattimes.model.LocationStatus
import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.ui.gps.GpsResultState
import il.soulSalttrader.shabbattimes.ui.gps.toLocationStatus
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class ToLocationStatusTest : DescribeSpec({
    describe("CARD_CONTENT_1 - SCENARIO: Card with granted permissions displays correct location status") {
        it("CARD_CONTENT_1_S1 - should map GpsResolved to LocationStatus.Current") {
            GpsResultState.Resolved(mockk()).toLocationStatus(PermissionState.Granted) shouldBe LocationStatus.Current
        }

        it("CARD_CONTENT_1_S2 - should map Loading to LocationStatus.Locating") {
            GpsResultState.Loading.toLocationStatus(PermissionState.Granted) shouldBe LocationStatus.Locating
        }

        it("CARD_CONTENT_1_S3 - should map Failure to LocationStatus.Unknown") {
            GpsResultState.Failure().toLocationStatus(PermissionState.Granted) shouldBe LocationStatus.Unknown
        }

        it("CARD_CONTENT_1_S4 - should map Idle to LocationStatus.Unknown") {
            GpsResultState.Idle.toLocationStatus(PermissionState.Granted) shouldBe LocationStatus.Unknown
        }

        it("CARD_CONTENT_1_S7 - should map Failure with actual cause to LocationStatus.Unknown") {
            GpsResultState.Failure(RuntimeException("Search failed")).toLocationStatus(PermissionState.Granted) shouldBe LocationStatus.Unknown
        }
    }

    describe("CARD_CONTENT_2 - SCENARIO: Card with denied permissions displays correct location status") {
        it("CARD_CONTENT_2_S1 - should map Idle with Denied permission to LocationStatus.NoPermission") {
            GpsResultState.Idle.toLocationStatus(PermissionState.Denied) shouldBe LocationStatus.NoPermission
        }

        it("CARD_CONTENT_2_S2 - should map Loading with Denied permission to LocationStatus.NoPermission") {
            GpsResultState.Loading.toLocationStatus(PermissionState.Denied) shouldBe LocationStatus.NoPermission
        }

        it("CARD_CONTENT_2_S5 - should map GpsResolved with Denied permission to LocationStatus.NoPermission") {
            GpsResultState.Resolved(mockk()).toLocationStatus(PermissionState.Denied) shouldBe LocationStatus.NoPermission
        }

        it("CARD_CONTENT_2_S6 - should map Failure with Denied permission to LocationStatus.NoPermission") {
            GpsResultState.Failure(RuntimeException("Search failed")).toLocationStatus(PermissionState.Denied) shouldBe LocationStatus.NoPermission
        }
    }
})

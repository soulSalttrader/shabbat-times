package il.soulSalttrader.shabbattimes.ui.search

import il.soulSalttrader.shabbattimes.model.LocationStatus
import il.soulSalttrader.shabbattimes.permission.PermissionState
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class ToLocationStatusTest : DescribeSpec( {
    describe("CARD_CONTENT_1 - SCENARIO: Card with granted permissions displays correct location status") {
        it("CARD_CONTENT_1_S1 - should map GpsResolved to LocationStatus.Current") {
            SearchResultState.GpsResolved(mockk()).toLocationStatus(PermissionState.Granted) shouldBe LocationStatus.Current
        }

        it("CARD_CONTENT_1_S2 - should map Loading to LocationStatus.Locating") {
            SearchResultState.Loading.toLocationStatus(PermissionState.Granted) shouldBe LocationStatus.Locating
        }

        it("CARD_CONTENT_1_S3 - should map Failure to LocationStatus.Unknown") {
            SearchResultState.Failure().toLocationStatus(PermissionState.Granted) shouldBe LocationStatus.Unknown
        }

        it("CARD_CONTENT_1_S4 - should map Idle to LocationStatus.Unknown") {
            SearchResultState.Idle.toLocationStatus(PermissionState.Granted) shouldBe LocationStatus.Unknown
        }

        it("CARD_CONTENT_1_S5 - should map Empty to LocationStatus.Unknown") {
            SearchResultState.Empty.toLocationStatus(PermissionState.Granted) shouldBe LocationStatus.Unknown
        }

        it("CARD_CONTENT_1_S6 - should map Suggestions to LocationStatus.Unknown") {
            SearchResultState.Suggestions(emptyList()).toLocationStatus(PermissionState.Granted) shouldBe LocationStatus.Unknown
        }

        it("CARD_CONTENT_1_S7 - should map Failure with actual cause to LocationStatus.Unknown") {
            SearchResultState.Failure(RuntimeException("Search failed")).toLocationStatus(PermissionState.Granted) shouldBe LocationStatus.Unknown
        }
    }

    describe("CARD_CONTENT_2 - SCENARIO: Card with denied permissions displays correct location status") {
        it("CARD_CONTENT_2_S1 - should map Idle with Denied permission to LocationStatus.NoPermission") {
            SearchResultState.Idle.toLocationStatus(PermissionState.Denied) shouldBe LocationStatus.NoPermission
        }

        it("CARD_CONTENT_2_S2 - should map Loading with Denied permission to LocationStatus.NoPermission") {
            SearchResultState.Loading.toLocationStatus(PermissionState.Denied) shouldBe LocationStatus.NoPermission
        }

        it("CARD_CONTENT_2_S3 - should map Empty with Denied permission to LocationStatus.NoPermission") {
            SearchResultState.Empty.toLocationStatus(PermissionState.Denied) shouldBe LocationStatus.NoPermission
        }

        it("CARD_CONTENT_2_S4 - should map Suggestions with Denied permission to LocationStatus.NoPermission") {
            SearchResultState.Suggestions(emptyList()).toLocationStatus(PermissionState.Denied) shouldBe LocationStatus.NoPermission
        }

        it("CARD_CONTENT_2_S5 - should map GpsResolved with Denied permission to LocationStatus.NoPermission") {
            SearchResultState.GpsResolved(mockk()).toLocationStatus(PermissionState.Denied) shouldBe LocationStatus.NoPermission
        }

        it("CARD_CONTENT_2_S6 - should map Failure with Denied permission to LocationStatus.NoPermission") {
            SearchResultState.Failure(RuntimeException("Search failed")).toLocationStatus(PermissionState.Denied) shouldBe LocationStatus.NoPermission
        }
    }
})

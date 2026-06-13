package il.soulSalttrader.shabbattimes.ui.search

import il.soulSalttrader.shabbattimes.model.LocationStatus
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class ToLocationStatusTest : DescribeSpec( {
    describe("CARD_CONTENT - SCENARIO: GPS Card displays correct location status") {
        it("CARD_CONTENT_S1 - should map GpsResolved to LocationStatus.Current") {
            SearchResultState.GpsResolved(mockk()).toLocationStatus() shouldBe LocationStatus.Current
        }

        it("CARD_CONTENT_S2 - should map Loading to LocationStatus.Locating") {
            SearchResultState.Loading.toLocationStatus() shouldBe LocationStatus.Locating
        }

        it("CARD_CONTENT_S3 - should map Failure to LocationStatus.Unknown") {
            SearchResultState.Failure().toLocationStatus() shouldBe LocationStatus.Unknown
        }

        it("CARD_CONTENT_S4 - should map Idle to LocationStatus.Unknown") {
            SearchResultState.Idle.toLocationStatus() shouldBe LocationStatus.Unknown
        }

        it("CARD_CONTENT_S5 - should map Empty to LocationStatus.Unknown") {
            SearchResultState.Empty.toLocationStatus() shouldBe LocationStatus.Unknown
        }

        it("CARD_CONTENT_S6 - should map Suggestions to LocationStatus.Unknown") {
            SearchResultState.Suggestions(emptyList()).toLocationStatus() shouldBe LocationStatus.Unknown
        }

        it("CARD_CONTENT_S7 - should map Failure with actual cause to LocationStatus.Unknown") {
            SearchResultState.Failure(RuntimeException("Search failed")).toLocationStatus() shouldBe LocationStatus.Unknown
        }
    }
})

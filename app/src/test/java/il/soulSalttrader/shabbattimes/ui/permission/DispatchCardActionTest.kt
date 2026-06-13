package il.soulSalttrader.shabbattimes.ui.permission

import il.soulSalttrader.shabbattimes.permission.PermissionState.Denied
import il.soulSalttrader.shabbattimes.permission.PermissionState.DeniedPermanently
import il.soulSalttrader.shabbattimes.permission.PermissionState.Granted
import il.soulSalttrader.shabbattimes.permission.PermissionState.Idle
import il.soulSalttrader.shabbattimes.ui.shabbat.CardAction
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class DispatchCardActionTest : DescribeSpec({
    describe("PERM_CARD_S3 - SCENARIO: Card click with granted permission opens GPS search") {
        it("PERM_CARD_S3_UI_1 - should return OpenGpsSearch when permission is Granted") {
            PermissionUiState(Granted).dispatchCardAction() shouldBe CardAction.OpenGpsSearch
        }
    }

    describe("PERM_CARD_S4 - SCENARIO: Card click with denied permission shows rationale flow") {
        it("PERM_CARD_S4_UI_1 - should return AcceptRationale when permission is Denied") {
            PermissionUiState(Denied).dispatchCardAction() shouldBe CardAction.AcceptRationale
        }
    }

    describe("PERM_CARD_S5 - SCENARIO: Card click with permanently denied permission shows settings dialog") {
        it("PERM_CARD_S5_UI_1 - should return ShowDeniedDialog when permission is DeniedPermanently") {
            PermissionUiState(DeniedPermanently).dispatchCardAction() shouldBe CardAction.ShowDeniedDialog
        }
    }

    describe("PERM_CARD_S6 - SCENARIO: Card click with idle permission starts education flow") {
        it("PERM_CARD_S6_UI_1 - should return ShowEducation for Idle") {
            PermissionUiState(Idle).dispatchCardAction() shouldBe CardAction.ShowEducation
        }
    }
})
package il.soulSalttrader.shabbattimes.ui.permission

import il.soulSalttrader.shabbattimes.permission.PermissionState.Denied
import il.soulSalttrader.shabbattimes.permission.PermissionState.DeniedPermanently
import il.soulSalttrader.shabbattimes.permission.PermissionState.DeniedPermanentlyRationale
import il.soulSalttrader.shabbattimes.permission.PermissionState.DeniedRationale
import il.soulSalttrader.shabbattimes.permission.PermissionState.Education
import il.soulSalttrader.shabbattimes.permission.PermissionState.Granted
import il.soulSalttrader.shabbattimes.permission.PermissionState.Idle
import il.soulSalttrader.shabbattimes.permission.PermissionState.Requesting
import il.soulSalttrader.shabbattimes.ui.shabbat.CardAction
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class DispatchCardActionTest : DescribeSpec({
    describe("PERM_CARD_S3 - SCENARIO: Card click with granted permission opens GPS search") {
        it("PERM_CARD_S3_UI_1 - should return OpenGpsSearch when permission is Granted") {
            PermissionUiState(Granted).permission.dispatchCardAction() shouldBe CardAction.OpenGpsSearch
        }
    }

    describe("PERM_CARD_S4 - SCENARIO: Card click with denied permission shows rationale flow") {
        it("PERM_CARD_S4_UI_1 - should return AcceptRationale when permission is Denied") {
            PermissionUiState(Denied).permission.dispatchCardAction() shouldBe CardAction.ShowDeniedRationale
        }
    }

    describe("PERM_CARD_S5 - SCENARIO: Card click with permanently denied permission shows settings dialog") {
        it("PERM_CARD_S5_UI_1 - should return ShowDeniedDialog when permission is DeniedPermanently") {
            PermissionUiState(DeniedPermanently).permission.dispatchCardAction() shouldBe CardAction.ShowDeniedPermanently
        }
    }

    describe("PERM_CARD_S6 - SCENARIO: Card click with idle permission starts education flow") {
        it("PERM_CARD_S6_UI_1 - should return PermissionRequested for Idle") {
            PermissionUiState(Idle).permission.dispatchCardAction() shouldBe CardAction.PermissionRequested
        }
    }

    describe("PERM_CARD_S7 - SCENARIO: Card click with Education permission starts education flow") {
        it("PERM_CARD_S7_UI_1 - should return PermissionRequested for Education") {
            PermissionUiState(Education).permission.dispatchCardAction() shouldBe CardAction.PermissionRequested
        }
    }

    describe("PERM_CARD_S8 - SCENARIO: Card click with Requesting permission has no action") {
        it("PERM_CARD_S8_UI_1 - should return None for Requesting") {
            PermissionUiState(Requesting).permission.dispatchCardAction() shouldBe CardAction.None
        }
    }

    describe("PERM_CARD_S9 - SCENARIO: Card click with DeniedRationale permission has no action") {
        it("PERM_CARD_S9_UI_1 - should return None for DeniedRationale") {
            PermissionUiState(DeniedRationale).permission.dispatchCardAction() shouldBe CardAction.None
        }
    }

    describe("PERM_CARD_S10 - SCENARIO: Card click with DeniedPermanentlyRationale permission has no action") {
        it("PERM_CARD_S10_UI_1 - should return None for DeniedPermanentlyRationale") {
            PermissionUiState(DeniedPermanentlyRationale).permission.dispatchCardAction() shouldBe CardAction.None
        }
    }
})
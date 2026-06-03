# PERM_FRESH_S1 - SCENARIO: User denies permission on first ask
## UI_DIALOG_S1 - should show system dialog after education dialog when permission denied
🤖

1. State = Denied (via fake repo)
2. Tap card
3. ✅ System permission dialog appears (Allow / Don't allow)

## UI_DIALOG_S2 - should show system dialog when permission denied
🤖

1. State = Denied (via fake repo)
2. Tap card
3. ✅ System permission dialog appears (Allow / Don't allow)

# PERM_FRESH_S3 - SCENARIO: User denies then allows via rationale
## UI_DIALOG_S3 - should show rationale dialog after system dialog denial
🖐️

1. Fresh install, tap card → Education → Continue → system dialog
2. Tap "Don't allow"
3. ✅ Rationale dialog appears with Allow / Cancel

> Cannot automate — system dialog kills/recreates activity making
> composeRule lose its reference. The state combination
> (Denied + isDialogVisible=true) requires the real system dialog
> interaction which is not reliably testable via UiAutomator.
> Covered at unit level by PERM_FRESH_S2 (DeniedWithRationale reducer test).

## UI_DIALOG_S4 - should show GPS card after allowing via rationale
🖐️

1. Deny on first ask → rationale dialog
2. Tap "Allow" → system dialog appears again
3. Grant permission
4. ✅ GPS card appears

# PERM_FRESH_S4 - SCENARIO: User permanently denies permission
## UI_DIALOG_S5 - should show permanently denied dialog after denying twice
🤖

1. Deny → rationale → deny again
2. ✅ Permanently denied dialog with "Open Settings" button
3. Tap "Add manually instead" → dismissed, no GPS card

## UI_DIALOG_S6 - should show system dialog when tapping outdated GPS card with denied permission
🤖

1. Revoke permission (via shell)
2. Launch app
3. ✅ Outdated GPS card with Last known location status, no dialogs
4. Tap card → system dialog appears

## UI_DIALOG_S7 - should show open settings dialog when tapping outdated GPS card with permanently denied permission
🤖📏

1. Permanently deny permission
2. Kill and reopen app
3. ✅ Outdated GPS card with Last known location status, no dialogs
4. Tap card → Open Settings dialog immediately (no Education step)
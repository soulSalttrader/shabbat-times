## PERM_EDGE_S1 - SCENARIO: Rapid tap GPS card
🖐️🎨📏 Unit (VM processes events correctly)

VM correctly processes rapid ShowEducation dispatches - state stays Education.
Deduplication guard (only one dialog) is HandlePermissions responsibility.

## PERM_EDGE_S2 - SCENARIO: Rotate screen during permission dialog
🖐️

1. System permission dialog visible
2. Rotate device
3. ✅ Dialog persists, result handled correctly

## PERM_EDGE_S3 - SCENARIO: Background app during permission dialog
🖐️

1. System permission dialog visible
2. Press home, return to app
3. ✅ Dialog still visible or state correctly restored

## PERM_EDGE_S4 - SCENARIO: Location limit reached with permission
📏 manual only

1. Add 7 saved locations
2. Try to add current location
3. ✅ Snackbar appears regardless of permission state

> PermissionViewModel has no knowledge of location limit.
> Location limit check is SearchViewModel responsibility.
> No PermissionViewModel unit test needed.

## PERM_EDGE_S5 - SCENARIO: Switch apps during system permission dialog
🖐️📏 (VM handles state transitions from Requesting correctly)

1. System dialog visible (state=Requesting)
2. Switch to another app
3. Return → ON_RESUME fires → resolvePermissionEvent()
4. ✅ State transitions correctly, no crash

> VM correctly handles AllGranted/DeniedWithRationale dispatched from Requesting state.
> Covered by PERM_FRESH_S1/S2 transition tests.
> Manual test needed for actual system dialog behavior across apps.

## PERM_EDGE_S6 - SCENARIO: Unrelated event should not touch permission

## PERM_EDGE_S7 - SCENARIO: AllGranted works from any denied state
# Fresh Install

## PERM_FRESH_S1 - Grant on first ask
📏

1. Fresh install, open app 
2. Tap card → Education dialog appears
3. Tap "Continue" → system permission dialog appears 
4. Tap "Allow"
5. ✅ GPS card appears with location

## PERM_FRESH_S2 - Deny on first ask
📏

1. Fresh install 
2. Tap GPS card → Education dialog 
3. Tap "Continue" → System dialog 
4. Tap "Don’t Allow"
5. ✅ Rationale dialog appears ("Why we need location")
6. Tap "Add manually instead" → dismissed

## PERM_FRESH_S3 - Deny then allow via rationale
📏

1. First ask denied 
2. Rationale shown 
3. Tap "Allow" → System dialog appears again 
4. Tap "Allow"
5. ✅ GPS card appears

## PERM_FRESH_S4 - Deny permanently (deny twice)
📏

1. First ask denied 
2. Rationale → "Allow" → deny again on system dialog 
3. ✅ Permanently denied dialog with "Open Settings" button 
4. Tap "Add manually instead" → dismissed

## PERM_FRESH_S5 - Dismiss system permission dialog
🖐️

⚠️ Not applicable for minSdk >= 29
System dialog cannot be dismissed without making a choice on API 29+.
Only relevant for older devices — manual test only if minSdk < 29.

## PERM_FRESH_S6 - Dismiss education dialog
🖐️

1. Tap card → Education dialog 
2. Swipe to dismiss 
3. ✅ Returns to Idle state 
4. Tap card again → Education dialog reappears

# App Settings Flow

## PERM_SETTINGS_S1 - Grant permission in Settings
📏🎨

1. In DeniedPermanently state 
2. Tap GPS card → "Open Settings" dialog 
3. Tap "Open Settings"
4. Enable permission → return to app 
5. ✅ State becomes Idle (or auto-resolves to Granted on next check)

## PERM_SETTINGS_S2 - Set to "Ask every time" in Settings
📏

1. App is in DeniedPermanently state
2. Tap GPS card → "Open Settings" dialog appears 
3. Open Settings → change to "Ask every time" / Allow all the time" → return 
4. ✅ State resets to Idle 
5. Tap card → Education dialog reappears

## PERM_SETTINGS_S3 - Ignore settings → return
📏

1. App is in DeniedPermanently state
2. Open Settings → ignore → return
3. ON_RESUME fires → resolvePermissionEvent() → null + DeniedPermanently
4. dispatch(ReturnedFromAppSettings) → state = Idle
5. User taps card → Education → Request → still DeniedPermanently
6. ✅ Open Settings dialog appears again

## PERM_SETTINGS_S4 — Kill app from settings → reopen
🖐️ (Part 1 — persistence: manual or instrumented test)
📏 (Part 2 — VM reaction: covered by PERM_RESTART_S1)

1. App in permanently denied state
2. Open Settings → grant permission
3. Kill app from settings
4. Reopen app
5. ✅ GPS card appears (repo rehydrates Granted → VM reflects it)

> Part 1 requires PermissionRepositoryImpl to persist state across process death.
> Part 2 is identical to PERM_RESTART_S1 — no separate unit test needed.

# App Restart Scenarios

## PERM_RESTART_S1 — Restart with granted permission
🖐️

1. State is NOT persisted in repo across process death. 
2. On cold start repo always starts Idle. 
3. Real permission state is resolved via PermissionHandler in HandlePermissions 
4. composable (LaunchedEffect + ON_RESUME). 
5. VM restart behavior is not unit testable — covered by manual/Espresso test.

## PERM_RESTART_S2 - Restart with temporary denial
🖐️

1. Deny permission, kill, reopen
2. ✅ No GPS card, no dialogs (repo=Idle, resolvePermissionEvent=DeniedWithRationale dispatched)
3. Tap card → rationale dialog appears

## PERM_RESTART_S3 - Restart with permanently denied
🖐️📏 (with persisted repo)

1. Permanently deny permission
2. Kill and reopen app
3. ✅ No GPS card, no dialogs on restart (state restored from DataStore)
4. Tap GPS card → Open Settings dialog appears immediately
   (no Education step — user already went through it)

## PERM_RESTART_S4 - Restart after revoking in settings
🖐️

1. Grant, revoke in settings, kill, reopen
2. ✅ No GPS card (resolvePermissionEvent=DeniedWithRationale dispatched on start)
3. Tap card → rationale dialog appears

# GPS Card Interactions

## PERM_CARD_S1 — Remove GPS card then re-add
📏 (permission unchanged on remove)
🖐️ (Education skipped when re-adding — driven by HandlePermissions composable)

1. Grant permission, GPS card visible
2. Swipe to remove GPS card 
3. ✅ GPS card disappears, permission unchanged 
4. Tap "Add current location"
5. ✅ GPS card reappears immediately (no permission dialog, already granted)

## PERM_CARD_S2 — Remove GPS card, revoke permission, re-add
🖐️

1. Grant permission, remove GPS card
2. Revoke permission in settings
3. Tap "Add current location"
4. ✅ Education dialog appears (driven by HandlePermissions —
   resolvePermissionEvent returns null, state=Idle → ShowEducation dispatched)

> VM behavior covered by PERM_FRESH_S1.
> Education trigger on re-add is HandlePermissions composable responsibility.

# Return After Time Away
> All return scenarios behave identically to restart scenarios at the VM level.
> Permission state is resolved on resume via HandlePermissions (ON_RESUME + LaunchedEffect).
> See PERM_RESTART_S1–S4 for VM unit tests.
> Manual testing required for time-based scenarios (auto-revoke, extended absence).

# Edge Cases

## PERM_EDGE_S1 — Rapid tap GPS card
🖐️🎨📏 Unit (VM processes events correctly)

VM correctly processes rapid ShowEducation dispatches — state stays Education.
Deduplication guard (only one dialog) is HandlePermissions responsibility.

## PERM_EDGE_S2 - Rotate screen during permission dialog
🎨

1. System permission dialog visible 
2. Rotate device 
3. ✅ Dialog persists, result handled correctly

## PERM_EDGE_S3 - Background app during permission dialog
🎨

1. System permission dialog visible 
2. Press home, return to app 
3. ✅ Dialog still visible or state correctly restored

## PERM_EDGE_S4 - Location limit reached + permission
📏 manual only

1. Add 7 saved locations
2. Try to add current location
3. ✅ Snackbar appears regardless of permission state

> PermissionViewModel has no knowledge of location limit.
> Location limit check is SearchViewModel responsibility.
> No PermissionViewModel unit test needed.

## PERM_EDGE_S5 - Switch apps during system permission dialog
🖐️📏 (VM handles state transitions from Requesting correctly)

1. System dialog visible (state=Requesting)
2. Switch to another app
3. Return → ON_RESUME fires → resolvePermissionEvent()
4. ✅ State transitions correctly, no crash

> VM correctly handles AllGranted/DeniedWithRationale dispatched from Requesting state.
> Covered by PERM_FRESH_S1/S2 transition tests.
> Manual test needed for actual system dialog behavior across apps.

## PERM_EDGE_S6 - Unrelated event should not touch permission

# PERM_MAPPING - PermissionChanged Mapping

## PERM_MAPPING_S1 — Idle state mapping
📏

1. Repository emits LocationPermission.Idle 
2. PermissionChanged event is processed 
3. ✅ UI State becomes PermissionState.Idle

## PERM_MAPPING_S2 — Education state mapping
📏

1. Repository emits LocationPermission.Education 
2. PermissionChanged event is processed 
3. ✅ UI State becomes PermissionState.Education

## PERM_MAPPING_S3 — Requesting state mapping
📏

1. Repository emits LocationPermission.Requesting 
2. PermissionChanged event is processed 
3. ✅ UI State becomes PermissionState.Requesting

## PERM_MAPPING_S4 — Granted state mapping
📏

1. Repository emits LocationPermission.Granted 
2. PermissionChanged event is processed 
3. ✅ UI State becomes PermissionState.Granted

## PERM_MAPPING_S5 — Denied (temporary) state mapping
📏

1. Repository emits LocationPermission.Denied 
2. PermissionChanged event is processed 
3. ✅ UI State becomes PermissionState.Denied

## PERM_MAPPING_S6 — DeniedPermanently state mapping
📏

1. Repository emits LocationPermission.DeniedPermanently 
2. PermissionChanged event is processed 
3. ✅ UI State becomes PermissionState.DeniedPermanently

# Bug

## PERM_COMBINE_S1 — ShowEducation never produces invalid intermediate state Idle+dialogVisible
📏 Unit (UnconfinedTestDispatcher)

> combine() fires twice on dispatch(ShowEducation):
> - first emission: permission=Idle, isDialogVisible=true  ← invalid
> - second emission: permission=Education, isDialogVisible=true  ← correct
    > UI may briefly recompose with invalid state causing dialog flash.
    > Seen in logs during manual testing.
    > Fix: separate _uiState (UI fields) from repo (permission) in combine,
    > and update repo before _uiState in dispatch().

## BUG_COMBINE_S2 — Idle flash on cold start with DeniedPermanently
📏 Unit (UnconfinedTestDispatcher)

> stateIn initialValue was hardcoded to Idle, causing one invalid Idle
> emission before combine produced the real persisted state.
> Fix: initialValue reads repo.permissionState.value synchronously.
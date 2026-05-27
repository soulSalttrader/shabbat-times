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
3. Open Settings → change to "Ask every time" → return 
4. ✅ State resets to Idle 
5. Tap card → System permission dialog appears

## PERM_SETTINGS_S3 - Return from Settings without change
📏

1. App is in DeniedPermanently state
2. Tap GPS card → "Open Settings" dialog appears
3. "Open Settings" dialog → tap "Open Settings"
4. Don't change anything → return to app 
5. ✅ Still shows permanently denied state, no crash

## PERM_SETTINGS_S4 - Kill app while in Settings
🖐️

1. App is in DeniedPermanently state
2. Tap GPS card → "Open Settings" dialog
3. Tap "Open Settings" → system settings opens
4. Change to "Allow all the time" or "Ask every time"
5. Kill app
6. Reopen app from launcher (cold start)
7. ✅ GPS card appears (allow) or permission dialog appears (ask every time)

# App Restart Scenarios

## PERM_RESTART_S1 - Restart with granted permission
📏🎨

1. Grant permission, GPS card visible 
2. Kill and reopen app 
3. ✅ GPS card appears immediately, no dialogs

## PERM_RESTART_S2 - Restart with temporary denial
📏

1. Deny permission, no GPS card 
2. Kill and reopen app 
3. ✅ No GPS card, no dialogs on restart 
4. Tap GPS card area → rationale dialog appears

## PERM_RESTART_S3 - Restart with permanently denied
📏

1. Permanently deny permission 
2. Kill and reopen app 
3. ✅ No GPS card, no dialogs on restart 
4. Tap GPS card area → permanently denied dialog appears

## PERM_RESTART_S4 - Restart after revoking in settings
📏

1. Grant permission, GPS card visible 
2. Go to system settings, revoke permission 
3. Kill and reopen app 
4. ✅ No GPS card, correct permission state restored

# GPS Card Interactions

## PERM_CARD_S1 - Remove GPS card then re-add (already granted)
📏🎨

1. Grant permission, GPS card visible
2. Swipe to remove GPS card 
3. ✅ GPS card disappears, permission unchanged 
4. Tap "Add current location"
5. ✅ GPS card reappears immediately (no permission dialog, already granted)

## PERM_CARD_S2 - Remove GPS card, revoke permission, then re-add
📏

1. Grant permission, remove GPS card 
2. Go to settings, revoke permission 
3. Tap "Add current location"
4. ✅ Permission flow starts from beginning (education dialog)

# Return After Time Away

## PERM_RETURN_S1 - Return after long time with granted permission
📏

1. Grant permission, close app 
2. Reopen after extended period 
3. ✅ GPS card appears, times updated

## PERM_RETURN_S2 - Return after long time with permission auto-revoked
📏

1. Grant permission, close app 
2. System auto-revokes permission (Android 11+ feature for unused apps)
3. Reopen app 
4. ✅ No GPS card, education dialog appears on tap

## PERM_RETURN_S3 - Return after long time with permanently denied
📏

1. Permanently deny, close app 
2. Reopen after extended period 
3. ✅ No GPS card, permanently denied dialog on tap, no spurious dialogs

# Edge Cases

## PERM_EDGE_S1 - Rapid taps on GPS card
📏

1. Tap GPS card multiple times quickly 
2. ✅ Only one permission dialog appears, no crashes

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
📏

1. Add 7 saved locations 
2. Try to add current location 
3. ✅ Snackbar appears regardless of permission state

## PERM_EDGE_S5 - Switch apps during system permission dialog
📏

1. Fresh install, open app
2. Tap on a Card → Education dialog appears
3. Tap "Continue" → system dialog appears
4. Switch to another app that also requests permission
5. Grant/deny permission in other app
6. Return to your app
7. ✅ System dialog still pending OR state = Idle, no crash

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
## PERM_SETTINGS_S1 - SCENARIO: User grants permission in settings
📏🎨

1. In DeniedPermanently state
2. Tap GPS card → "Open Settings" dialog
3. Tap "Open Settings"
4. Enable permission → return to app
5. ✅ State becomes Idle (or auto-resolves to Granted on next check)

## PERM_SETTINGS_S2 - SCENARIO: User sets Ask every time in settings
📏

1. App is in DeniedPermanently state
2. Tap GPS card → "Open Settings" dialog appears
3. Open Settings → change to "Ask every time" / Allow all the time" → return
4. ✅ State resets to Idle
5. Tap card → Education dialog reappears

## PERM_SETTINGS_S3 - SCENARIO: User ignores settings and returns
📏

1. App is in DeniedPermanently state
2. Open Settings → ignore → return
3. ON_RESUME fires → resolvePermissionEvent() → null + DeniedPermanently
4. dispatch(ReturnedFromAppSettings) → state = Idle
5. User taps card → Education → Request → still DeniedPermanently
6. ✅ Open Settings dialog appears again

## PERM_SETTINGS_S4 - SCENARIO: User kills app from settings
🖐️ (Part 1 - persistence: manual or instrumented test)
📏 (Part 2 - VM reaction: covered by PERM_RESTART_S1)

1. App in permanently denied state
2. Open Settings → grant permission
3. Kill app from settings
4. Reopen app
5. ✅ GPS card appears (repo rehydrates Granted → VM reflects it)

> Part 1 requires PermissionRepositoryImpl to persist state across process death.
> Part 2 is identical to PERM_RESTART_S1 - no separate unit test needed.
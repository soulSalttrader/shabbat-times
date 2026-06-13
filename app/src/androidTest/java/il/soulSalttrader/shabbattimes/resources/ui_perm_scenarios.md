# PERM_RESTART_S1 - SCENARIO: Restart with granted permission
## UI_PERM_RESTART_S1 - should show GPS card on relaunch when permission granted
🤖

1. Grant permission (via shell)
2. Launch app
3. ✅ GPS card appears automatically, no dialogs

# PERM_SETTINGS_S1 - SCENARIO: User grants permission in settings
## UI_PERM_SETTINGS_S1 - should show GPS card after granting permission in settings
🖐️

1. Permanently denied state
2. Tap card → Open Settings dialog
3. Tap "Open Settings" → system settings
4. Grant permission → return
5. ✅ GPS card appears

# PERM_SETTINGS_S2 - SCENARIO: User sets Ask every time in settings
## UI_PERM_SETTINGS_S2 - should reset state to Idle after returning from settings
🖐️

1. Permanently denied state
2. Open Settings → ignore → return
3. ✅ State resets to Idle
4. Tap card → Education dialog (not Open Settings)
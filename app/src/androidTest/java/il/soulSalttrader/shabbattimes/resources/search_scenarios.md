# UI / Instrumented Test Scenarios

# Permission Flow

## UI_PERM_FRESH_S1 - GPS card appears after granting permission
🤖

1. Fresh install (no permissions)
2. App launches → no GPS card
3. Tap card → Education dialog appears
4. Tap "Continue" → system dialog appears
5. Grant permission
6. ✅ GPS card appears automatically

## UI_PERM_FRESH_S2 - system dialog appears after education dialog when permission denied
🤖

1. State = Denied (via fake repo)
2. Tap card
3. ✅ System permission dialog appears (Allow / Don't allow)

## UI_PERM_FRESH_S3 - system dialog appears when permission denied
🤖

1. State = Denied (via fake repo)
2. Tap card
3. ✅ System permission dialog appears (Allow / Don't allow)

## UI_PERM_FRESH_S4 - rationale dialog appears after system dialog denial
🖐️

1. Fresh install, tap card → Education → Continue → system dialog
2. Tap "Don't allow"
3. ✅ Rationale dialog appears with Allow / Cancel

> Cannot automate — system dialog kills/recreates activity making
> composeRule lose its reference. The state combination
> (Denied + isDialogVisible=true) requires the real system dialog
> interaction which is not reliably testable via UiAutomator.
> Covered at unit level by PERM_FRESH_S2 (DeniedWithRationale reducer test).

## UI_PERM_FRESH_S5 - GPS card appears after rationale → allow
🖐️

1. Deny on first ask → rationale dialog
2. Tap "Allow" → system dialog appears again
3. Grant permission
4. ✅ GPS card appears

## UI_PERM_FRESH_S6 - Permanently denied dialog appears after denying twice
🤖

1. Deny → rationale → deny again
2. ✅ Permanently denied dialog with "Open Settings" button
3. Tap "Add manually instead" → dismissed, no GPS card

---

# App State

## UI_PERM_RESTART_S1 - GPS card visible on relaunch when permission granted
🤖

1. Grant permission (via shell)
2. Launch app
3. ✅ GPS card appears automatically, no dialogs

## UI_PERM_RESTART_S2 - Outdated GPS card visible and tapping shows system dialog when denied
🤖

1. Revoke permission (via shell)
2. Launch app
3. ✅ Outdated GPS card with Last known location status, no dialogs
4. Tap card → system dialog appears

## UI_PERM_RESTART_S3 - Outdated GPS card visible and tapping shows open settings dialog
🤖📏

1. Permanently deny permission
2. Kill and reopen app
3. ✅ Outdated GPS card with Last known location status, no dialogs
4. Tap card → Open Settings dialog immediately (no Education step)

---

# Card UI

## UI_CARD_S1 - Empty card shown when no locations saved
🤖

1. Fresh state (no locations)
2. ✅ Empty card visible
3. ✅ No drag handle on empty card

## UI_CARD_S2 - GPS card shown when permission granted
🤖

1. Grant permission (via shell)
2. Launch app
3. ✅ GPS card visible
4. ✅ Drag handle visible on GPS card

## UI_CARD_S3 - Location card shown after adding location
🤖

1. Add location via search
2. ✅ Location card visible with city name
3. ✅ Drag handle visible on location card

## UI_CARD_S4 - GPS card disappears after swipe to delete
🤖🖐️

1. Grant permission, GPS card visible
2. Swipe GPS card left
3. Confirm deletion dialog
4. ✅ GPS card removed, empty card appears

---

# Search Flow

## UI_SEARCH_S1 - Location added from search suggestion
🤖 ⚠️ BUG_SEARCH_ADD_LOCATION_S1

1. Open search
2. Type city name
3. Select suggestion
4. Close search
5. ✅ Location card appears once in list

## UI_SEARCH_S2 - Search closed without selection
🤖

1. Open search
2. Type city name
3. Close without selecting
4. ✅ No new card added

---

# Settings Flow

## UI_PERM_SETTINGS_S1 - GPS card appears after granting in settings
🖐️

1. Permanently denied state
2. Tap card → Open Settings dialog
3. Tap "Open Settings" → system settings
4. Grant permission → return
5. ✅ GPS card appears

## UI_PERM_SETTINGS_S2 - State resets to Idle after returning from settings
🖐️

1. Permanently denied state
2. Open Settings → ignore → return
3. ✅ State resets to Idle
4. Tap card → Education dialog (not Open Settings)

# Search Flow

## SEARCH_ADD_LOCATION_S1 — Add new location from search suggestion
🔧🎨

1. Open search
2. Type city name
3. Wait for suggestions
4. Select suggestion
5. Close search
6. ✅ Location card appears once in the list

---

## Bug

## BUG_SEARCH_ADD_LOCATION_S1 — Location saved twice on suggestion select
🎨 Instrumented (FakeSavedLocationsRepository)

> Exposed by fake repository which has no duplicate guard unlike Room.
> save() is called twice when suggestion is selected:
> - first call: save(Brno), current: []
> - second call: save(Brno), current: [Brno]
    > Production hidden by Room's OnConflictStrategy.IGNORE silently rejecting duplicate key.
    > Fake correctly reveals the real double-dispatch behavior.
    > Fix: find and remove duplicate save() call in SearchViewModel dispatch handling.
    > Related: SEARCH_ADD_LOCATION_S1
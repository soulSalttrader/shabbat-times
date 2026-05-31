# UI / Instrumented Test Scenarios

# Permission Flow

## UI_PERM_FRESH_S1 - should show GPS card after granting permission
🤖

1. Fresh install (no permissions)
2. App launches → no GPS card
3. Tap card → Education dialog appears
4. Tap "Continue" → system dialog appears
5. Grant permission
6. ✅ GPS card appears automatically

## UI_PERM_FRESH_S2 - should show system dialog after education dialog when permission denied
🤖

1. State = Denied (via fake repo)
2. Tap card
3. ✅ System permission dialog appears (Allow / Don't allow)

## UI_PERM_FRESH_S3 - should show system dialog when permission denied
🤖

1. State = Denied (via fake repo)
2. Tap card
3. ✅ System permission dialog appears (Allow / Don't allow)

## UI_PERM_FRESH_S4 - should show rationale dialog after system dialog denial
🖐️

1. Fresh install, tap card → Education → Continue → system dialog
2. Tap "Don't allow"
3. ✅ Rationale dialog appears with Allow / Cancel

> Cannot automate — system dialog kills/recreates activity making
> composeRule lose its reference. The state combination
> (Denied + isDialogVisible=true) requires the real system dialog
> interaction which is not reliably testable via UiAutomator.
> Covered at unit level by PERM_FRESH_S2 (DeniedWithRationale reducer test).

## UI_PERM_FRESH_S5 - should show GPS card after allowing via rationale
🖐️

1. Deny on first ask → rationale dialog
2. Tap "Allow" → system dialog appears again
3. Grant permission
4. ✅ GPS card appears

## UI_PERM_FRESH_S6 - should show permanently denied dialog after denying twice
🤖

1. Deny → rationale → deny again
2. ✅ Permanently denied dialog with "Open Settings" button
3. Tap "Add manually instead" → dismissed, no GPS card

---

# App State

## UI_PERM_RESTART_S1 - should show GPS card on relaunch when permission granted
🤖

1. Grant permission (via shell)
2. Launch app
3. ✅ GPS card appears automatically, no dialogs

## UI_PERM_RESTART_S2 - should show system dialog when tapping outdated GPS card with denied permission
🤖

1. Revoke permission (via shell)
2. Launch app
3. ✅ Outdated GPS card with Last known location status, no dialogs
4. Tap card → system dialog appears

## UI_PERM_RESTART_S3 - should show open settings dialog when tapping outdated GPS card with permanently denied permission
🤖📏

1. Permanently deny permission
2. Kill and reopen app
3. ✅ Outdated GPS card with Last known location status, no dialogs
4. Tap card → Open Settings dialog immediately (no Education step)

---

# Card UI

## UI_CARD_S1 - should show empty card when no locations saved
🤖

1. Fresh state (no locations)
2. ✅ Empty card visible
3. ✅ No drag handle on empty card

## UI_CARD_S2 - should show GPS card when permission granted
🤖

1. Grant permission (via shell)
2. Launch app
3. ✅ GPS card visible
4. ✅ Drag handle visible on GPS card

> Covered by UI_PERM_FRESH_S1 - GPS card appears after granting permission

## UI_CARD_S3 - should show location card after adding location
🤖

1. Add location via search
2. ✅ Location card visible with city name
3. ✅ Drag handle visible on location card

> Covered by SEARCH_ADD_LOCATION_S1 - should successfully add new location from search suggestion

# Card UI Scenarios

## UI_CARD_SWIPE_S1 - should show delete confirmation dialog when swiped left
🤖

1. Add location card
2. Swipe card left
3. ✅ Delete confirmation dialog appears

## UI_CARD_SWIPE_S2 - should remove card when delete confirmed
🤖

1. Add location card
2. Swipe left → confirmation dialog
3. Tap confirm
4. ✅ Card removed, empty card appears

## UI_CARD_SWIPE_S3 - should keep card when delete dismissed
🤖

1. Add location card
2. Swipe left → confirmation dialog
3. Tap dismiss
4. ✅ Card still visible

## UI_CARD_SWIPE_S4 - should remove GPS card when swiped and confirmed
🤖

1. GPS card visible
2. Swipe left → confirmation dialog
3. Tap confirm
4. ✅ GPS card removed, empty card appears

---

## UI_CARD_REORDER_S1 - should change order when card dragged up
🖐️ (gesture not automatable)
🔧 Partially covered by REPO_REORDER_S1 (persistence logic)

1. Add two location cards (A, B)
2. Drag B above A
3. ✅ Order is B, A in repo

> ReorderableItem uses custom pointer input not triggerable
> via Compose test performTouchInput.
> Drag handle presence is verified automatically in UI_CARD_CONTENT_S5/S6.

> Drag gesture can't be automated via performTouchInput.
> Reorder persistence is verified at repository level.
> UI order change requires manual verification.

## UI_CARD_REORDER_S2 - should change order when card dragged down
🖐️ see UI_CARD_REORDER_S1

## UI_CARD_REORDER_S3 - should allow GPS card to be reordered
🖐️ see UI_CARD_REORDER_S1

---

## UI_CARD_CONTENT_S1 - should display location name on card
🤖

1. Add location "Brno"
2. ✅ "Brno" text visible on card

## UI_CARD_CONTENT_S2 - should display shabbat times on card
🤖

1. Add location card with times
2. ✅ Candle lighting and havdalah times visible

## UI_CARD_CONTENT_S3 - should show current location label on GPS card
🤖

1. GPS card visible
2. ✅ Current location status label displayed

## UI_CARD_CONTENT_S4 - should show add location prompt on empty card
🤖

1. No locations saved
2. ✅ Add location prompt visible on empty card

## UI_CARD_CONTENT_S5 - should show drag handle on GPS card
🤖

1. GPS card visible
2. ✅ Drag handle icon present on GPS card

## UI_CARD_CONTENT_S6 - should show drag handle on location card
🤖

1. Location card visible
2. ✅ Drag handle icon present on location card

## UI_CARD_CONTENT_S7 - should not show drag handle on empty card
🤖

1. No locations saved
2. ✅ Empty card visible, no drag handle

---

# Search Flow

## UI_SEARCH_S1 - should add location from search suggestion
🤖

1. Open search
2. Type city name
3. Select suggestion
4. Close search
5. ✅ Location card appears once in list

## UI_SEARCH_S2 - should not add location when search closed without selection
🤖

1. Open search
2. Type city name
3. Close without selecting
4. ✅ No new card added

---

# Settings Flow

## UI_PERM_SETTINGS_S1 - should show GPS card after granting permission in settings
🖐️

1. Permanently denied state
2. Tap card → Open Settings dialog
3. Tap "Open Settings" → system settings
4. Grant permission → return
5. ✅ GPS card appears

## UI_PERM_SETTINGS_S2 - should reset state to Idle after returning from settings
🖐️

1. Permanently denied state
2. Open Settings → ignore → return
3. ✅ State resets to Idle
4. Tap card → Education dialog (not Open Settings)

---

# Repository

## REPO_REORDER_S1 - should persist new sort order after reorder
🔧 (SavedLocationsRepositoryRoomTest)

1. Save [ID1, ID2, ID3]
2. Reorder to [ID1, ID3, ID2]
3. ✅ DAO returns [ID1, ID3, ID2] in correct order

## REPO_REORDER_S2 - should persist sort order when card moved down
🔧 (SavedLocationsRepositoryRoomTest)

1. Save [ID1, ID2, ID3]
2. Reorder to [ID3, ID2, ID1] → move ID1 down
3. ✅ DAO returns [ID3, ID2, ID1] with updated sortOrder

## REPO_REORDER_S3 - should persist sort order when GPS card reordered
🔧 (SavedLocationsRepositoryRoomTest)

1. Save [GPS, ID1, ID2, ID3]
2. Reorder to [ID1, ID2, GPS, ID3]
3. ✅ DAO returns [ID1, ID2, GPS, ID3] with correct sortOrder

---

# Bug

## BUG_SEARCH_S1 — Location saved twice on suggestion select
⚠️🎨 (FakeSavedLocationsRepository)

> Exposed by fake repository which has no duplicate guard unlike Room.
> save() is called twice when suggestion is selected:
> - first call: save(Brno), current: []
> - second call: save(Brno), current: [Brno]
    > Production hidden by Room's OnConflictStrategy.IGNORE silently rejecting duplicate key.
    > Fake correctly reveals the real double-dispatch behavior.
    > Fix: find and remove duplicate save() call in SearchViewModel dispatch handling.
    > Related: SEARCH_ADD_LOCATION_S1

## ✅ UI_PERM_FRESH_S1 - should show GPS card after granting permission
## ✅ UI_PERM_FRESH_S2 - should show system dialog after education dialog when permission denied
## ✅ UI_PERM_FRESH_S3 - should show system dialog when permission denied
## 🖐️ UI_PERM_FRESH_S4 - should show rationale dialog after system dialog denial
## 🖐️ UI_PERM_FRESH_S5 - should show GPS card after allowing via rationale
## ✅ UI_PERM_FRESH_S6 - should show permanently denied dialog after denying twice

## ✅ UI_PERM_RESTART_S1 - should show GPS card on relaunch when permission granted
## ✅ UI_PERM_RESTART_S2 - should show system dialog when tapping outdated GPS card with denied permission
## ✅ UI_PERM_RESTART_S3 - should show open settings dialog when tapping outdated GPS card with permanently denied permission

## ✅ UI_CARD_S1 - should show empty card when no locations saved
## ✅ UI_CARD_S2 - should show GPS card when permission granted > UI_PERM_FRESH_S1
## ✅ UI_CARD_S3 - should show location card after adding location > UI_SEARCH_S1

## ✅ UI_CARD_SWIPE_S1 - should show delete confirmation dialog when swiped left
## ✅ UI_CARD_SWIPE_S2 - should remove card when delete confirmed
## ✅ UI_CARD_SWIPE_S3 - should keep card when delete dismissed
## ✅ UI_CARD_SWIPE_S4 - should remove GPS card when swiped and confirmed

## 🖐️ UI_CARD_REORDER_S1 - should change order when card dragged up > Partially covered by REPO_REORDER_S1 (persistence logic)
## 🖐️ UI_CARD_REORDER_S2 - should change order when card dragged down > Partially covered by REPO_REORDER_S2 (persistence logic)
## 🖐️ UI_CARD_REORDER_S3 - should allow GPS card to be reordered > Partially covered by REPO_REORDER_S3 (persistence logic)

## UI_CARD_CONTENT_S1 - should display location name on card
## UI_CARD_CONTENT_S2 - should display shabbat times on card
## UI_CARD_CONTENT_S3 - should show current location label on GPS card
## UI_CARD_CONTENT_S4 - should show add location prompt on empty card
## ✅ UI_CARD_CONTENT_S5 - should show drag handle on GPS card
## ✅ UI_CARD_CONTENT_S6 - should show drag handle on location card
## ✅ UI_CARD_CONTENT_S7 - should not show drag handle on empty card

## ⚠️ UI_SEARCH_S1 - should add location from search suggestion
## UI_SEARCH_S2 - should not add location when search closed without selection

## UI_PERM_SETTINGS_S1 - should show GPS card after granting permission in settings
## UI_PERM_SETTINGS_S2 - should reset state to Idle after returning from settings

## ✅ REPO_REORDER_S1 - should persist new sort order after reorder
## ✅ REPO_REORDER_S2 - should persist sort order when card moved down
## REPO_REORDER_S3 - should persist sort order when GPS card reordered

## ⚠️ BUG_SEARCH_ADD_LOCATION_S1 — Location saved twice on suggestion select
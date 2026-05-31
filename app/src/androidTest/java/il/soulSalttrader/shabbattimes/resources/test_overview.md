## ✅ UI_PERM_FRESH_S1 - GPS card appears after granting permission
## ✅ UI_PERM_FRESH_S2 - system dialog appears after education dialog when permission denied
## ✅ UI_PERM_FRESH_S3 - system dialog appears when permission denied
## 🖐️ UI_PERM_FRESH_S4 - rationale dialog appears after system dialog denial
## 🖐️ UI_PERM_FRESH_S5 - GPS card appears after rationale → allow
## ✅ UI_PERM_FRESH_S6 - Permanently denied dialog appears after denying twice

## ✅ UI_PERM_RESTART_S1 - GPS card visible on relaunch when permission granted
## ✅ UI_PERM_RESTART_S2 - Outdated GPS card visible and tapping shows system dialog when denied
## ✅ UI_PERM_RESTART_S3 - Outdated GPS card visible and tapping shows open settings dialog

## ✅ UI_CARD_S1 - Empty card shown when no locations saved
## ✅ UI_CARD_S2 - GPS card shown when permission granted > UI_PERM_FRESH_S1
## ✅ UI_CARD_S3 - Location card shown after adding location > UI_SEARCH_S1

## ✅ UI_CARD_SWIPE_S1 - Swipe left shows delete confirmation dialog
## ✅ UI_CARD_SWIPE_S2 - Confirm delete removes card
## ✅ UI_CARD_SWIPE_S3 - Dismiss delete keeps card
## ✅ UI_CARD_SWIPE_S4 - GPS card swipe removes GPS card

## 🖐️ UI_CARD_REORDER_S1 - Drag card up changes order > Partially covered by REPO_REORDER_S1 (persistence logic)
## 🖐️ UI_CARD_REORDER_S2 - Drag card down changes order > Partially covered by REPO_REORDER_S2 (persistence logic)
## 🖐️ UI_CARD_REORDER_S3 - GPS card can be reordered > Partially covered by REPO_REORDER_S3 (persistence logic)

## UI_CARD_CONTENT_S1 - Location name displayed on card
## UI_CARD_CONTENT_S2 - Shabbat times displayed on card
## UI_CARD_CONTENT_S3 - GPS card shows current location label
## UI_CARD_CONTENT_S4 - Empty card shows add location prompt
## ✅ UI_CARD_CONTENT_S5 - Drag handle visible on GPS card
## ✅ UI_CARD_CONTENT_S6 - Drag handle visible on location card
## ✅ UI_CARD_CONTENT_S7 - No drag handle on empty card

## ✅ UI_SEARCH_S1 - Location added from search suggestion
## UI_SEARCH_S2 - Search closed without selection

## UI_PERM_SETTINGS_S1 - GPS card appears after granting in settings
## UI_PERM_SETTINGS_S2 - State resets to Idle after returning from settings

## ⚠️ SEARCH_ADD_LOCATION_S1 — Add new location from search suggestion > BUG_SEARCH_ADD_LOCATION_S1

## ✅ REPO_REORDER_S1 - Reorder persists new sort order
## ✅ REPO_REORDER_S2 - Drag card down persists new sort order
## REPO_REORDER_S3 - GPS card reorder persists correctly

## ⚠️ BUG_SEARCH_ADD_LOCATION_S1 — Location saved twice on suggestion select
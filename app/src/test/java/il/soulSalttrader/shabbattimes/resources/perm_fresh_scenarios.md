## PERM_FRESH_S1 - SCENARIO: User grants permission on first ask
📏

1. Fresh install, open app
2. Tap card → Education dialog appears
3. Tap "Continue" → system permission dialog appears
4. Tap "Allow"
5. ✅ GPS card appears with location

## PERM_FRESH_S2 - SCENARIO: User denies permission on first ask
📏

1. Fresh install
2. Tap GPS card → Education dialog
3. Tap "Continue" → System dialog
4. Tap "Don’t Allow"
5. ✅ Rationale dialog appears ("Why we need location")
6. Tap "Add manually instead" → dismissed

## PERM_FRESH_S3 - SCENARIO: User denies then allows via rationale
PERM_FRESH_S3_REDUCER_1 - should set Requesting on AcceptedRationale
PERM_FRESH_S3_VM - should reflect Granted after deny → accept rationale → grant
UI_PERM_FRESH_S4 - should show rationale dialog after system dialog denial
UI_PERM_FRESH_S5 - should show GPS card after allowing via rationale
📏

1. First ask denied
2. Rationale shown
3. Tap "Allow" → System dialog appears again
4. Tap "Allow"
5. ✅ GPS card appears

## PERM_FRESH_S4 - SCENARIO: User permanently denies permission
📏

1. First ask denied
2. Rationale → "Allow" → deny again on system dialog
3. ✅ Permanently denied dialog with "Open Settings" button
4. Tap "Add manually instead" → dismissed

## PERM_FRESH_S5 - SCENARIO: User dismisses system dialog without choosing
🖐️

⚠️ Not applicable for minSdk >= 29
System dialog cannot be dismissed without making a choice on API 29+.
Only relevant for older devices - manual test only if minSdk < 29.

## PERM_FRESH_S6 - SCENARIO: User dismisses education dialog
🖐️

1. Tap card → Education dialog
2. Swipe to dismiss
3. ✅ Returns to Idle state
4. Tap card again → Education dialog reappears
## PERM_CARD_S1 - SCENARIO: Remove GPS card then re-add
## 
📏 (permission unchanged on remove)
🖐️ (Education skipped when re-adding - driven by HandlePermissions composable)

1. Grant permission, GPS card visible
2. Swipe to remove GPS card
3. ✅ GPS card disappears, permission unchanged
4. Tap "Add current location"
5. ✅ GPS card reappears immediately (no permission dialog, already granted)

## PERM_CARD_S2 - SCENARIO: Remove GPS card, revoke permission, re-add
🖐️

1. Grant permission, remove GPS card
2. Revoke permission in settings
3. Tap "Add current location"
4. ✅ Education dialog appears (driven by HandlePermissions -
   resolvePermissionEvent returns null, state=Idle → ShowEducation dispatched)

> VM behavior covered by PERM_FRESH_S1.
> Education trigger on re-add is HandlePermissions composable responsibility.
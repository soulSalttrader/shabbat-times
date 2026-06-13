# PERM_FRESH_S1 - SCENARIO: User grants permission on first ask
## PERM_HANDLER_S1 - should return AllGranted when all permissions granted
📏

1. All permissions granted
2. resolvePermissionEvent()
3. ✅ Returns AllGranted

1. Fresh install, open app
2. Tap card → Education dialog appears
3. Tap "Continue" → system permission dialog appears
4. Tap "Allow"
5. ✅ GPS card appears with location

# PERM_FRESH_S2 - SCENARIO: User denies permission on first ask
## PERM_HANDLER_S2 - should return DeniedWithRationale when all denied with rationale
📏

1. No permissions granted
2. shouldShowRationale = true for all
3. resolvePermissionEvent()
4. ✅ Returns DeniedWithRationale

# PERM_FRESH_S4 - SCENARIO: User permanently denies permission
## PERM_HANDLER_S3 - should return null when all denied without rationale
📏

1. No permissions granted
2. shouldShowRationale = false for all
3. resolvePermissionEvent()
4. ✅ Returns null
   (can't distinguish permanent denial from fresh install)
   (repo state DeniedPermanently vs Idle makes the distinction)

# SCENARIO: Permission resolution handles partial grants correctly
## PERM_HANDLER_S4 - should return DeniedWithRationale when partially granted with rationale
📏

1. COARSE granted, FINE denied with rationale
2. resolvePermissionEvent()
3. ✅ Returns DeniedWithRationale
   (any denied permission with rationale → show rationale)

## PERM_HANDLER_S5 - should return null when partially granted without rationale
📏

1. COARSE granted, FINE denied without rationale
2. resolvePermissionEvent()
3. ✅ Returns null
   (can't distinguish from fresh install — repo state decides)

## PERM_HANDLER_S6 - should return DeniedWithRationale when any permission has rationale
📏

1. No permissions granted
2. COARSE has rationale, FINE does not
3. resolvePermissionEvent()
4. ✅ Returns DeniedWithRationale
   (any permission with rationale → show rationale dialog)
   (rationale takes priority over null)
## ✅ PERM_HANDLER_S1 - should return AllGranted when all permissions granted
## ✅ PERM_HANDLER_S2 - should return DeniedWithRationale when all denied with rationale
## ✅ PERM_HANDLER_S3 - should return null when all denied without rationale
## ✅ PERM_HANDLER_S4 - should return DeniedWithRationale when partially granted with rationale
## ✅ PERM_HANDLER_S5 - should return null when partially granted without rationale
## ✅ PERM_HANDLER_S6 - should return DeniedWithRationale when any permission has rationale

## ✅ PERM_FRESH_S1 - Grant on first ask
## ✅ PERM_FRESH_S2 - Deny on first ask
## ✅ PERM_FRESH_S3 - Deny then allow via rationale
## ✅ PERM_FRESH_S4 - Deny permanently (deny twice)
## 🖐️ PERM_FRESH_S5 - Dismiss system permission dialog 🔧
## ❓ PERM_FRESH_S6 - Dismiss education dialog

## ✅ PERM_SETTINGS_S1 - Grant permission in Settings
## ✅ PERM_SETTINGS_S2 - Set to "Ask every time" in Settings
## ✅ PERM_SETTINGS_S3 - Ignore settings → return
## PERM_SETTINGS_S4 — Kill app from settings → reopen

## ✅ PERM_RESTART_S1 — Restart with granted permission
## ✅ PERM_RESTART_S2 - Restart with temporary denial
## ✅ PERM_RESTART_S3 - Restart with permanently denied
## PERM_RESTART_S4 - Restart after revoking in settings

## ✅ PERM_CARD_S1 - Remove GPS card then re-add
## ✅ PERM_CARD_S2 - Remove GPS card, revoke permission, re-add

## ✅ PERM_EDGE_S1 - Rapid tap GPS card
## PERM_EDGE_S2 - Rotate screen during permission dialog
## PERM_EDGE_S3 - Background app during permission dialog
## PERM_EDGE_S4 - Location limit reached + permission
## ✅ PERM_EDGE_S5 - Switch apps during system permission dialog
## PERM_EDGE_S6 - Unrelated event should not touch permission

## ✅ PERM_MAPPING_S1 - Idle state mapping
## ✅ PERM_MAPPING_S2 - Education state mapping
## ✅ PERM_MAPPING_S3 - Requesting state mapping
## ✅ PERM_MAPPING_S4 - Granted state mapping
## ✅ PERM_MAPPING_S5 - Denied (temporary) state mapping
## ✅ PERM_MAPPING_S6 - DeniedPermanently state mapping

## PERM_COMBINE_S1 - ShowEducation never produces invalid intermediate state Idle+dialogVisible
## ⚠️ BUG_COMBINE_S2 - Idle flash on cold start with DeniedPermanently

## ✅ USECASE_REORDER_S1 - should move first item to the end
## ✅ USECASE_REORDER_S2 - should move item from last to first
## ✅ USECASE_REORDER_S3 - should move item in middle of list
## ✅ USECASE_REORDER_S4 - should move item to same position
## ✅ USECASE_REORDER_S5 - should reorder GPS card
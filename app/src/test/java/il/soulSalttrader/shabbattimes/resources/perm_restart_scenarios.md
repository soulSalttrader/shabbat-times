## PERM_RESTART_S1 - SCENARIO: Restart with granted permission
🖐️

1. State is NOT persisted in repo across process death.
2. On cold start repo always starts Idle.
3. Real permission state is resolved via PermissionHandler in HandlePermissions
4. composable (LaunchedEffect + ON_RESUME).
5. VM restart behavior is not unit testable - covered by manual/Espresso test.

## PERM_RESTART_S2 - SCENARIO: Restart with temporary denial
🖐️

1. Deny permission, kill, reopen
2. ✅ No GPS card, no dialogs (repo=Idle, resolvePermissionEvent=DeniedWithRationale dispatched)
3. Tap card → rationale dialog appears

## PERM_RESTART_S3 - SCENARIO: Restart with permanently denied
🖐️📏 (with persisted repo)

1. Permanently deny permission
2. Kill and reopen app
3. ✅ No GPS card, no dialogs on restart (state restored from DataStore)
4. Tap GPS card → Open Settings dialog appears immediately
   (no Education step - user already went through it)

## PERM_RESTART_S4 - SCENARIO: Restart after revoking in settings
🖐️

1. Grant, revoke in settings, kill, reopen
2. ✅ No GPS card (resolvePermissionEvent=DeniedWithRationale dispatched on start)
3. Tap card → rationale dialog appears
# SCENARIO: LocationPermission maps correctly to PermissionState
## PERM_MAPPING_S1 - should map Idle to PermissionState.Idle
📏

1. Repository emits LocationPermission.Idle
2. PermissionChanged event is processed
3. ✅ UI State becomes PermissionState.Idle

## PERM_MAPPING_S2 - should map Education to PermissionState.Education
📏

1. Repository emits LocationPermission.Education
2. PermissionChanged event is processed
3. ✅ UI State becomes PermissionState.Education

## PERM_MAPPING_S3 - should map Requesting to PermissionState.Requesting
📏

1. Repository emits LocationPermission.Requesting
2. PermissionChanged event is processed
3. ✅ UI State becomes PermissionState.Requesting

## PERM_MAPPING_S4 - should map Granted to PermissionState.Granted
📏

1. Repository emits LocationPermission.Granted
2. PermissionChanged event is processed
3. ✅ UI State becomes PermissionState.Granted

## PERM_MAPPING_S5 - should map Denied to PermissionState.Denied
📏

1. Repository emits LocationPermission.Denied
2. PermissionChanged event is processed
3. ✅ UI State becomes PermissionState.Denied

## PERM_MAPPING_S6 - should map DeniedPermanently to PermissionState.DeniedPermanently
📏

1. Repository emits LocationPermission.DeniedPermanently
2. PermissionChanged event is processed
3. ✅ UI State becomes PermissionState.DeniedPermanently
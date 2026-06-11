## ✅ PERM_COMBINE_S1_VM - SCENARIO: combine() fires twice causing invalid intermediate state

> combine() fires twice on dispatch(ShowEducation):
> - first emission: permission=Idle, isDialogVisible=true  ← invalid
> - second emission: permission=Education, isDialogVisible=true  ← correct
    > UI may briefly recompose with invalid state causing dialog flash.
    > Seen in logs during manual testing.
    > Fix: separate _uiState (UI fields) from repo (permission) in combine,
    > and update repo before _uiState in dispatch().

## ✅ BUG_COMBINE_S2 - SCENARIO: Idle flash on cold start with DeniedPermanently

> stateIn initialValue was hardcoded to Idle, causing one invalid Idle
> emission before combine produced the real persisted state.
> Fix: initialValue reads repo.permissionState.value synchronously.

## ✅ PERM_SETTINGS_S1_VM_1 - should set Idle after returning from settings
## ✅ PERM_SETTINGS_S1_VM_2 - should start fresh Education flow when card is tapped after returning from Settings
## ✅ PERM_SETTINGS_S2_VM_1 - should show Education dialog on next tap after user grants it in Settings
## ✅ PERM_SETTINGS_S3_VM_1 - should keep DeniedPermanently when settings ignored
## ✅ PERM_SETTINGS_S1_REDUCER_2 - should reset to Idle on ReturnedFromAppSettings
## ✅ PERM_SETTINGS_S1_REDUCER_3 - should start fresh Education flow after returning from settings
## ✅ PERM_SETTINGS_S2_REDUCER_1 - should reset to Idle from any state on ReturnedFromAppSettings
## ✅ PERM_HANDLER_S3 - should return null when all denied without rationale
## ✅ PERM_HANDLER_S5 - should return null when partially granted without rationale
## ✅ PERM_HANDLER_S6 - should return DeniedWithRationale when any permission has rationale

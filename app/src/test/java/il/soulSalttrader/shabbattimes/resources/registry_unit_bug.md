## PERM_COMBINE_S1 - SCENARIO: combine() fires twice causing invalid intermediate state
📏 Unit (UnconfinedTestDispatcher)

> combine() fires twice on dispatch(ShowEducation):
> - first emission: permission=Idle, isDialogVisible=true  ← invalid
> - second emission: permission=Education, isDialogVisible=true  ← correct
    > UI may briefly recompose with invalid state causing dialog flash.
    > Seen in logs during manual testing.
    > Fix: separate _uiState (UI fields) from repo (permission) in combine,
    > and update repo before _uiState in dispatch().

## BUG_COMBINE_S2 - SCENARIO: Idle flash on cold start with DeniedPermanently
📏 Unit (UnconfinedTestDispatcher)

> stateIn initialValue was hardcoded to Idle, causing one invalid Idle
> emission before combine produced the real persisted state.
> Fix: initialValue reads repo.permissionState.value synchronously.
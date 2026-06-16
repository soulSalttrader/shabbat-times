# Scenario Registry

## Legend
- ✅ Automated
- 🖐️ Manual only  
- ⚠️ Known bug
- ❓ Not implemented
- 🔧 Unit test
- 🤖 Instrumented test

---

# Permission — Fresh Install

## SCENARIO: User grants permission on first ask
PERM_FRESH_S1

| Layer | Slug                                                                                           | Status |
|---|------------------------------------------------------------------------------------------------|---|
| Reducer | ~ PERM_FRESH_S1_REDUCER_1 - should set Education and show dialog on ShowEducation              | ✅ |
| Reducer | ~ PERM_FRESH_S1_REDUCER_2 - should set Requesting and show dialog on Request                   | ✅ |
| Reducer | ~ PERM_FRESH_S1_REDUCER_3 - should set Granted on AllGranted                                   | ✅ |
| VM | ~ PERM_FRESH_S1_VM_1 - should reflect Granted after full Education → Requesting → Granted flow | ✅ |
| VM | ~ PERM_FRESH_S1_VM_2 - should keep isDialogVisible after repo emission following ShowEducation | ✅ |
| Handler | ~ PERM_HANDLER_S1 - should return AllGranted when all permissions granted                      | ✅ |
| UI | ~ UI_CARD_S2_1 - should show GPS card when permission granted                                  | ✅ |

## SCENARIO: User denies permission on first ask
PERM_FRESH_S2

| Layer | Slug                                                                                     | Status |
|---|------------------------------------------------------------------------------------------|---|
| Reducer | ~ PERM_FRESH_S2_REDUCER_1 - should set Denied on DeniedWithRationale                     | ✅ |
| Reducer | ~ PERM_FRESH_S2_REDUCER_2 - should hide dialog and keep Denied on DismissedRationale     | ✅ |
| VM | ~ PERM_FRESH_S2_VM - should show Denied state after deny flow                            | ✅ |
| Handler | ~ PERM_HANDLER_S2 - should return DeniedWithRationale when all denied with rationale     | ✅ |
| UI | ~ UI_DIALOG_S1 - should show system dialog after education dialog | ✅ |
| UI | ~ UI_DIALOG_S2 - should show rationale dialog when system permission denied                       | 🖐️ |

## SCENARIO: User denies then allows via rationale
PERM_FRESH_S3

| Layer | Slug                                                                              | Status |
|---|-----------------------------------------------------------------------------------|---|
| Reducer | ~ PERM_FRESH_S3_REDUCER_1 - should set Requesting on AcceptedRationale            | ✅ |
| VM | ~ PERM_FRESH_S3_VM - should reflect Granted after deny → accept rationale → grant | ✅ |
| UI | ~ UI_DIALOG_S3 - should show rationale dialog after system dialog denial          | 🖐️ |

## SCENARIO: User permanently denies permission
PERM_FRESH_S4

| Layer | Slug                                                                                                                | Status               |
|---|---------------------------------------------------------------------------------------------------------------------|----------------------|
| Reducer | ~ PERM_FRESH_S4_REDUCER_1 - should set DeniedPermanently on DeniedPermanently                                       | ✅                    |
| VM | ~ PERM_FRESH_S4_VM - should reflect DeniedPermanently after denying twice                                           | ✅                    |
| Handler | ~ PERM_HANDLER_S3 - should return null when all denied without rationale                                            | ✅                    |
| UI | ~ UI_DIALOG_S4 - should show GPS card after allowing via rationale                                                  | ✅ → see UI_CARD_S2_1 |
| UI | ~ UI_DIALOG_S5 - should show permanently denied dialog after denying twice                                          | ✅                    |
| UI | ~ UI_DIALOG_S6 - should show system dialog when tapping outdated GPS card with denied permission                    | ✅                    |
| UI | ~ UI_DIALOG_S7 - should show open settings dialog when tapping outdated GPS card with permanently denied permission | ✅                    |

## SCENARIO: User dismisses system dialog without choosing
PERM_FRESH_S5

| Layer | Slug                                                                                  | Status |
|---|---------------------------------------------------------------------------------------|---|
| Reducer | ~ PERM_FRESH_S5_REDUCER_1 - should keep Requesting state when system dialog dismissed | ✅ |
| VM | ~ PERM_FRESH_S5_VM - should keep Requesting state when system dialog dismissed        | ✅ |
| UI | —                                                                                     | ⚠️ Not applicable minSdk >= 29 |

## SCENARIO: User dismisses education dialog
PERM_FRESH_S6

| Layer | Slug                                                                                                            | Status |
|---|-----------------------------------------------------------------------------------------------------------------|--|
| VM | ~ PERM_FRESH_S6_VM - should return to Idle with dialog hidden when Education dialog dismissed | ✅ |
| UI | ~ UI_DIALOG_S8 - should show Education dialog when card tapped with Idle permission                             | ✅ |
| UI | ~ UI_DIALOG_S9 - should keep empty card when Education dialog is dismissed                                      | ✅ |

---

## SCENARIO: Permission resolution handles partial grants correctly
PERM_HANDLER_PARTIAL

| Layer   | Slug                                                                                        | Status |
|---------|---------------------------------------------------------------------------------------------|--|
| Handler | ~ PERM_HANDLER_S4 - should return DeniedWithRationale when partially granted with rationale | ✅ |
| Handler | ~ PERM_HANDLER_S5 - should return null when partially granted without rationale             | ✅ |
| Handler | ~ PERM_HANDLER_S6 - should return DeniedWithRationale when any permission has rationale     | ✅ |

---

# Permission — App Settings Flow

## SCENARIO: User grants permission in settings
PERM_SETTINGS_S1

| Layer | Slug                                                                                                          | Status |
|---|---------------------------------------------------------------------------------------------------------------|---|
| Reducer | ~ PERM_SETTINGS_S1_REDUCER_1 - should set DeniedPermanently and show dialog on ShowDeniedPermanentlyDialog    | ✅ |
| Reducer | ~ PERM_SETTINGS_S1_REDUCER_2 - should reset to Idle on ReturnedFromAppSettings                                | ✅ |
| Reducer | ~ PERM_SETTINGS_S1_REDUCER_3 - should start fresh Education flow after returning from settings                | ✅ |
| VM | ~ PERM_SETTINGS_S1_VM_1 - should set Idle after returning from settings                                       | ✅ |
| VM | ~ PERM_SETTINGS_S1_VM_2 - should start fresh Education flow when card is tapped after returning from Settings | ✅ |
| UI | ~ UI_PERM_SETTINGS_S1 - should show GPS card after granting permission in settings                            | 🖐️ |

## SCENARIO: User sets Ask every time in settings
PERM_SETTINGS_S2

| Layer | Slug                                                                                               | Status |
|---|----------------------------------------------------------------------------------------------------|---|
| Reducer | ~ PERM_SETTINGS_S2_REDUCER_1 - should reset to Idle from any state on ReturnedFromAppSettings      | ✅ |
| VM | ~ PERM_SETTINGS_S2_VM_1: should show Education dialog on next tap after user grants it in Settings | ✅ |
| UI | ~ UI_PERM_SETTINGS_S2 - should reset state to Idle after returning from settings                   | 🖐️ |

## SCENARIO: User ignores settings and returns
PERM_SETTINGS_S3

| Layer | Slug                                                                                 | Status |
|---|--------------------------------------------------------------------------------------|---|
| Reducer | ~ PERM_SETTINGS_S3_REDUCER_1 - should keep DeniedPermanently on RequestedAppSettings | ✅ |
| VM | ~ PERM_SETTINGS_S3_VM_1 - should keep DeniedPermanently when settings ignored        | ✅ |
| UI | —                                                                                    | 🖐️ |

---

## SCENARIO: User kills app from settings
PERM_SETTINGS_S4

| Layer | Slug                                                                                 | Status |
|---|--------------------------------------------------------------------------------------|---|
| VM | ~ PERM_SETTINGS_S4_VM_1 - should reflect Granted on cold start after settings change | ❓ |
| UI | —                                                                                    | 🖐️ |

---

# Permission — App Restart

## SCENARIO: Restart with granted permission
PERM_RESTART_S1

| Layer | Slug                                                                      | Status |
|---|---------------------------------------------------------------------------|---|
| VM | ~ PERM_RESTART_S1_VM_1 - should reflect Granted immediately on cold start | ✅ |
| UI | ~ UI_CARD_S2_2 - should show GPS card on relaunch when permission granted | ✅ |

---

## SCENARIO: Restart with temporary denial
PERM_RESTART_S2

| Layer | Slug                                                                                             | Status |
|---|--------------------------------------------------------------------------------------------------|---|
| VM | ~ PERM_RESTART_S2_VM_1 - should reflect Denied on cold start, no dialogs                         | ✅ |
| UI | ~ UI_DIALOG_S6 - should show system dialog when tapping outdated GPS card with denied permission | ✅ |

---

## SCENARIO: Restart with permanently denied
PERM_RESTART_S3

| Layer | Slug                                                                                                                         | Status |
|---|------------------------------------------------------------------------------------------------------------------------------|---|
| VM | ~ PERM_RESTART_S3_VM_1 - should reflect DeniedPermanently on cold start                                                      | ✅ |
| VM | ~ PERM_RESTART_S3_VM_2 - should skip Education and show Open Settings after cold start when permission is permanently denied | ✅ |
| UI | ~ UI_DIALOG_S7 - should show open settings dialog when tapping outdated GPS card with permanently denied permission          | ✅ |

---

## SCENARIO: Restart after revoking in settings
PERM_RESTART_S4

| Layer | Slug                                                                   | Status |
|---|------------------------------------------------------------------------|---|
| VM | ~ PERM_RESTART_S4_VM - should reflect Denied after external revocation | ✅ |
| UI | —                                                                      | 🖐️ |

---

# Permission — Card Interactions

## SCENARIO: Remove GPS card then re-add
PERM_CARD_S1

| Layer | Slug                                                                                              | Status |
|-------|---------------------------------------------------------------------------------------------------|--|
| VM    | ~ PERM_CARD_S1_VM_1 - should call removeLocationUseCase when GPS card deleted                     | ✅ |
| VM    | ~ PERM_CARD_S1_VM_2 - should call removeLocationUseCase with isCurrent false for non-GPS card     | ✅ |
| VM    | ~ PERM_CARD_S1_UC_1 - should keep permission state unchanged when removing a non-current location | ✅ |
| UI    | —                                                                                                 | 🖐️ |

## SCENARIO: Remove GPS card, revoke permission, re-add
PERM_CARD_S2

| Layer    | Slug                                                                                       | Status               |
|----------|--------------------------------------------------------------------------------------------|----------------------|
| VM       | ~ PERM_CARD_S2_VM_1 - should show Education flow when permission resets to Idle            | ✅ → see PERM_CARD_S6 |
| Use Case | ~ PERM_CARD_S2_UC_1 - should reset permission state to Idle when removing current location | ✅                    |
| Use Case       | ~ PERM_CARD_S2_UC_2 - should not reset permission state when removing non-current location | ✅                 |

## SCENARIO: Card click with granted permission opens GPS search
PERM_CARD_S3

| Layer | Slug                                                                         | Status |
|-------|------------------------------------------------------------------------------|--|
| Presentation mapping    | ~ PERM_CARD_S3_UI_1 - should return OpenGpsSearch when permission is Granted | ✅ |

## SCENARIO: Card click with denied permission shows rationale flow
PERM_CARD_S4

| Layer | Slug                                                                          | Status |
|-------|-------------------------------------------------------------------------------|--|
| Presentation mapping    | ~ PERM_CARD_S4_UI_1 - should return AcceptRationale when permission is Denied | ✅ |

## SCENARIO: Card click with permanently denied permission shows settings dialog
PERM_CARD_S5

| Layer | Slug                                                                                      | Status |
|-------|-------------------------------------------------------------------------------------------|--|
| Presentation mapping    | ~ PERM_CARD_S5_UI_1 - should return ShowDeniedDialog when permission is DeniedPermanently | ✅ |

## SCENARIO: Card click with idle permission starts education flow
PERM_CARD_S6

| Layer | Slug                                                       | Status |
|-------|------------------------------------------------------------|--|
| Presentation mapping    | ~ PERM_CARD_S6_UI_1 - should return ShowEducation for Idle | ✅ |

---

# Card Content

## SCENARIO: GPS Card displays correct location status
CARD_CONTENT

| Layer | Slug                                                                               | Status |
|-------|------------------------------------------------------------------------------------|---|
| Unit  | ~ CARD_CONTENT_S1 - should map GpsResolved to LocationStatus.Current               | ✅ |
| Unit  | ~ CARD_CONTENT_S2 - should map Loading to LocationStatus.Locating                  | ✅ |
| Unit  | ~ CARD_CONTENT_S3 - should map Failure to LocationStatus.Unknown                   | ✅ |
| Unit  | ~ CARD_CONTENT_S4 - should map Idle to LocationStatus.Unknown                      | ✅ |
| Unit  | ~ CARD_CONTENT_S5 - should map Empty to LocationStatus.Unknown                     | ✅ |
| Unit  | ~ CARD_CONTENT_S6 - should map Suggestions to LocationStatus.Unknown               | ✅ |
| Unit  | ~ CARD_CONTENT_S7 - should map Failure with actual cause to LocationStatus.Unknown | ✅ |

## SCENARIO: Reorder cards via drag handle
CARD_REORDER

| Layer | Slug                                                       | Status |
|---|------------------------------------------------------------|---|
| UseCase | ~ USECASE_REORDER_S1 - should move first item to the end   | ✅ |
| UseCase | ~ USECASE_REORDER_S2 - should move item from last to first | ✅ |
| UseCase | ~ USECASE_REORDER_S3 - should move item in middle of list  | ✅ |
| UseCase | ~ USECASE_REORDER_S4 - should move item to same position   | ✅ |
| UseCase | ~ USECASE_REORDER_S5 - should reorder GPS card             | ✅ |

---

# Permission — Edge Cases

## SCENARIO: Rapid tap GPS card
PERM_EDGE_S1

| Layer | Slug                                                                   | Status |
|---|------------------------------------------------------------------------|---|
| VM | ~ PERM_EDGE_S1_VM - should not cause invalid state on rapid dispatches | ✅ |
| UI | —                                                                      | 🖐️ |

---

## SCENARIO: Rotate screen during permission dialog
PERM_EDGE_S2

| Layer | Slug | Status |
|---|---|---|
| VM | ~ PERM_EDGE_S2_VM_1 - should preserve Education state after configuration change | ✅ |
| VM | ~ PERM_EDGE_S2_VM_2 - should preserve DeniedPermanently state after configuration change | ✅ |
| UI | ~ UI_PERM_EDGE_S2_1 - should preserve Education dialog after rotation | 🖐️ |
| UI | ~ UI_PERM_EDGE_S2_2 - should preserve Open Settings dialog after rotation | 🖐️ |

---

## SCENARIO: Background app during permission dialog
PERM_EDGE_S3

| Layer | Slug | Status |
|---|---|---|
| VM | ~ PERM_EDGE_S3_VM_1 - should keep Requesting state after app backgrounded during permission request | ✅ |
| UI | ~ UI_PERM_EDGE_S3_1 - should preserve Education dialog when returning from background | 🖐️ |
| UI | ~ UI_PERM_EDGE_S3_2 - should preserve Open Settings dialog when returning from background | 🖐️ |

> Requesting is a transient state — system dialog is dismissed when app
> is backgrounded. On return, HandlePermissions resolves real OS state.
> VM correctly resets to Idle, not Requesting.

---

## SCENARIO: Location limit reached with permission
PERM_EDGE_S4

| Layer    | Slug                                                                                                  | Status |
|----------|-------------------------------------------------------------------------------------------------------|---|
| Use Case | ~ PERM_EDGE_S4_UC_1 - should return LimitReached when location limit is reached |  ✅ |
| Use Case | ~ PERM_EDGE_S4_UC_2 - should save location and return Success when limit not reached |  ✅ |
| UI       | ~ UI_PERM_EDGE_S4_1 - should show snackbar when tapping add location at limit                         | 🖐️ |

---

## SCENARIO: Switch apps during system permission dialog
PERM_EDGE_S5

| Layer | Slug                                                                                 | Status |
|---|--------------------------------------------------------------------------------------|---|
| VM | ~ PERM_EDGE_S5_VM - should keep Requesting state when interrupted                    | ✅ |
| UI | ~ UI_PERM_EDGE_S5_1 - should show system dialog again when returning from other app  | 🖐️ |

---

## SCENARIO: Unrelated event should not touch permission
PERM_EDGE_S6

| Layer   | Slug                                                                          | Status |
|---------|-------------------------------------------------------------------------------|---|
| VM      | ~ PERM_EDGE_S6_VM - should not change permission state on unrelated event     | ✅ |
| Reducer | ~ PERM_EDGE_S6_REDUCER_1 - should not change Granted state on unrelated event |✅|

## SCENARIO: AllGranted works from any denied state
PERM_EDGE_S7

| Layer   | Slug                                                                               | Status |
|---------|------------------------------------------------------------------------------------|--|
| Reducer | ~ PERM_EDGE_S7_REDUCER_2 - should set Granted from DeniedPermanently on AllGranted           |✅|

---

# Permission — State Mapping

## SCENARIO: LocationPermission maps correctly to PermissionState
PERM_MAPPING

| Layer | Slug                                                                                  | Status |
|---|---------------------------------------------------------------------------------------|---|
| Reducer | ~ PERM_MAPPING_S1 - should map Idle to PermissionState.Idle                           | ✅ |
| Reducer | ~ PERM_MAPPING_S2 - should map Education to PermissionState.Education                 | ✅ |
| Reducer | ~ PERM_MAPPING_S3 - should map Requesting to PermissionState.Requesting               | ✅ |
| Reducer | ~ PERM_MAPPING_S4 - should map Granted to PermissionState.Granted                     | ✅ |
| Reducer | ~ PERM_MAPPING_S5 - should map Denied to PermissionState.Denied                       | ✅ |
| Reducer | ~ PERM_MAPPING_S6 - should map DeniedPermanently to PermissionState.DeniedPermanently | ✅ |
| VM | ~ PERM_MAPPING_VM - should reflect correct state for each repo emission               | ✅ |

---

# Permission — Bug Regression

## SCENARIO: combine() fires twice causing invalid intermediate state
PERM_COMBINE_S1

| Layer | Slug                                                                              | Status |
|---|-----------------------------------------------------------------------------------|---|
| VM | ~ PERM_COMBINE_S1_VM - should never produce Idle+dialogVisible intermediate state | ✅ |

---

## SCENARIO: Idle flash on cold start with DeniedPermanently
PERM_COMBINE_S2

| Layer | Slug                                                                                | Status |
|---|-------------------------------------------------------------------------------------|---|
| VM | ~ PERM_COMBINE_S2_VM - should not flash Idle before DeniedPermanently on cold start | ✅ |

---

# Solar Ephemeris Calculator

## SCENARIO: Compute solar declination
EPHEMERIS-NOAA-S1

| Layer      | Slug                                                                                | Status |
|------------|-------------------------------------------------------------------------------------|---|
| Calculator | ~ EPHEMERIS-NOAA-S1_1 - should returns declination near -22.84° on the perihelion | ✅ |
| Calculator | ~ EPHEMERIS-NOAA-S1_2 - should returns declination near 0° at March equinox | ✅ |
| Calculator | ~ EPHEMERIS-NOAA-S1_3 - should returns declination near +23.44° at June solstice | ✅ |
| Calculator | ~ EPHEMERIS-NOAA-S1_4 - should returns declination near 22.83° on the aphelion | ✅ |
| Calculator | ~ EPHEMERIS-NOAA-S1_5 - should returns declination near 0° at September equinox | ✅ |
| Calculator | ~ EPHEMERIS-NOAA-S1_6 - should returns declination near -23.44° at December solstice | ✅ |

## SCENARIO: Compute Equation of time
EPHEMERIS-NOAA-S2

| Layer | Slug                                                                                | Status |
|--|-------------------------------------------------------------------------------------|---|
| Calculator | ~ EPHEMERIS-NOAA-S2_1 - equation of time should be near -4.5 min on the Perihelion | ✅ |
| Calculator | ~ EPHEMERIS-NOAA-S2_2 - equation of time should be near -7.3 min on March equinox | ✅ |
| Calculator | ~ EPHEMERIS-NOAA-S2_3 - equation of time should be near -1.7 min on June solstice | ✅ |
| Calculator | ~ EPHEMERIS-NOAA-S2_4 - equation of time should be near -4.5 min on the aphelion | ✅ |
| Calculator | ~ EPHEMERIS-NOAA-S2_5 - equation of time should be near +7.2 min on the September equinox | ✅ |
| Calculator | ~ EPHEMERIS-NOAA-S2_6 - equation of time should be near +1.7 min on December solstice | ✅ |

---

## SCENARIO: Compute Solar Altitude Calculations
EPHEMERIS-ALTITUDE-S1

| Layer | Slug                                                                                | Status |
|--|-------------------------------------------------------------------------------------|---|
| Calculator | ~ EPHEMERIS-ALTITUDE-S1_1 - should be near zenith (89.99°) at Tropic of Cancer on Summer Solstice 2026 | ❓ |
| Calculator | ~ EPHEMERIS-ALTITUDE-S1_2 - should be near zenith (89.99°) at Tropic of Capricorn on Winter Solstice 2026 | ❓ |
| Calculator | ~ EPHEMERIS-ALTITUDE-S1_3 - should show Midnight Sun (~ +23.44°) at North Pole on Summer Solstice 2026" | ❓ |
| Calculator | ~ EPHEMERIS-ALTITUDE-S1_4 - should show polar night (~ -23.44°) at South Pole on Winter Solstice 2026 | ❓ |
| Calculator | ~ EPHEMERIS-ALTITUDE-S1_5 - should have correct altitude (~ 23.38) at Greenwich during Equation of Time peak (Autumn 2026) | ❓ |
| Calculator | ~ EPHEMERIS-ALTITUDE-S1_6 - should be near horizon (~ 1.8°) at International Date Line on March Equinox | ❓ |
| Calculator | ~ EPHEMERIS-ALTITUDE-S1_7 - should have correct noon altitude (~ 61.35° ) in Prague on Summer Solstice 2026 | ❓ |

---
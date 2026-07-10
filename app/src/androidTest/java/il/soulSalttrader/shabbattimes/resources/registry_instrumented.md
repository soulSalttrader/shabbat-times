# Scenario Registry

## Legend
- ✅ Automated
- 🖐️ Manual only
- ⚠️ Known bug
- ❓ Not implemented
- 🔧 Unit test
- 🤖 Instrumented test

---

# Card UI

## SCENARIO: Empty card shown when no locations saved
UI_CARD_S1

| Layer | Slug                                                          | Status |
|---|---------------------------------------------------------------|---|
| UI | ~ UI_CARD_S1 - should show empty card when no locations saved | ✅ |

---

## SCENARIO: GPS card shown when permission granted
UI_CARD_S2

| Layer | Slug                                                                      | Status |
|---|---------------------------------------------------------------------------|---|
| UI | ~ UI_CARD_S2_1 - should show GPS card when permission granted             | ✅ |
| UI | ~ UI_CARD_S2_2 - should show GPS card on relaunch when permission granted | ✅ |

---

## SCENARIO: Show location card after adding location
UI_CARD_S3

| Layer | Slug                                                           | Status       |
|---|----------------------------------------------------------------|--------------|
| UI | ~ UI_CARD_S3 - should show location card after adding location | ✅ → see UI_SEARCH_S1 |

---

# Card — Content

## SCENARIO: Card displays correct content
UI_CARD_CONTENT

| Layer | Slug                                                                  | Status |
|---|-----------------------------------------------------------------------|---|
| UI | ~ UI_CARD_CONTENT_S1 - should display location name on card           | ✅ |
| UI | ~ UI_CARD_CONTENT_S2 - should display shabbat times on card           | ✅ |
| UI | ~ UI_CARD_CONTENT_S3 - should show current location label on GPS card | ✅ |
| UI | ~ UI_CARD_CONTENT_S4 - should show add location prompt on empty card  | ✅ |
| UI | ~ UI_CARD_CONTENT_S5 - should show drag handle on GPS card            | ✅ |
| UI | ~ UI_CARD_CONTENT_S6 - should show drag handle on location card       | ✅ |
| UI | ~ UI_CARD_CONTENT_S7 - should not show drag handle on empty card      | ✅ |

---

# Card — Swipe

## SCENARIO: Swipe card to delete
UI_CARD_SWIPE

| Layer | Slug                                                                         | Status |
|---|------------------------------------------------------------------------------|---|
| UI | ~ UI_CARD_SWIPE_S1 - should show delete confirmation dialog when swiped left | ✅ |
| UI | ~ UI_CARD_SWIPE_S2 - should remove card when delete confirmed                | ✅ |
| UI | ~ UI_CARD_SWIPE_S3 - should keep card when delete dismissed                  | ✅ |
| UI | ~ UI_CARD_SWIPE_S4 - should remove GPS card when swiped and confirmed        | ✅ |

---

# Card — Reorder

## SCENARIO: Reorder cards via drag handle
UI_CARD_REORDER

| Layer   | Slug                                                                       | Status |
|---------|----------------------------------------------------------------------------|--|
| Repo    | ~ REPO_CARD_REORDER_S1 - should persist new sort order after reorder       | ✅|
| Repo    | ~ REPO_CARD_REORDER_S2 - should persist sort order when card moved down    | ✅ |
| Repo    | ~ REPO_CARD_REORDER_S3 - should persist sort order when GPS card reordered | ✅ |
| UI      | ~ UI_CARD_REORDER_S1 - should change order when card dragged up            | 🖐️ |
| UI      | ~ UI_CARD_REORDER_S2 - should change order when card dragged down          | 🖐️ |
| UI      | ~ UI_CARD_REORDER_S3 - should allow GPS card to be reordered               | 🖐️ |

---

# Search

## SCENARIO: Add location from search
UI_SEARCH_S1

| Layer | Slug                                                        | Status |
|---|-------------------------------------------------------------|---|
| UI | ~ UI_SEARCH_S1 - should add location from search suggestion | ✅ |

---

## SCENARIO: Close search without selection
UI_SEARCH_S2

| Layer | Slug                                                                          | Status |
|---|-------------------------------------------------------------------------------|---|
| UI | ~ UI_SEARCH_S2 - should not add location when search closed without selection | ✅ |

---

# SCENARIO: should never dispatch same event multiple times consecutively
PERM_DISPATCH_S1

| Layer | Slug                                                                                                    | Status |
|---|---------------------------------------------------------------------------------------------------------|---|
| UI | ~ PERM_DISPATCH_S1_1 - should dispatch AllGranted exactly once                                          | ✅ |
| UI | ~ PERM_DISPATCH_S1_2 - should dispatch DeniedWithRationale exactly once                                 | ✅ |
| UI | ~ PERM_DISPATCH_S1_3 - should call onSettingsHandled when returnedFromSettings is true                  | ✅ |
| UI | ~ PERM_DISPATCH_S1_4 - should not call onSettingsHandled when returnedFromSettings is false             | ✅ |
| UI | ~ PERM_DISPATCH_S1_5 - should dispatch event only once even if ON_RESUME fires multiple times           | ✅ |
| UI | ~ PERM_DISPATCH_S1_6 - should dispatch SystemGranted once on initial composition when already granted   | ✅ |
| UI | ~ PERM_DISPATCH_S1_7 - should not dispatch anything on initial composition when not granted             | ✅ |
| UI | ~ PERM_DISPATCH_S1_8 - should not check granted state on initial composition when permission is not Idle | ✅ |
| UI | ~ PERM_DISPATCH_S1_9 - should not re-dispatch SystemGranted on recomposition                            | ✅ |

---

## SCENARIO: Observer lifecycle
NETWORK_1

| Layer    | Slug                                                                                             | Status |
|----------|--------------------------------------------------------------------------------------------------|--------|
| Observer | ~ NETWORK_3_S1 - awaitClose unregisters the callback when subscribers drop to zero after timeout | ❓      |
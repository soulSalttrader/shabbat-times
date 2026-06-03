## UI_CARD_S1 - SCENARIO: Empty card shown when no locations saved
🤖

1. Fresh state (no locations)
2. ✅ Empty card visible
3. ✅ No drag handle on empty card

## UI_CARD_S2 - SCENARIO: GPS card shown when permission granted
🤖

1. Grant permission (via shell)
2. Launch app
3. ✅ GPS card visible
4. ✅ Drag handle visible on GPS card

> Covered by PERM_FRESH_S1 - GPS card appears after granting permission

## UI_CARD_S3 - SCENARIO: Show location card after adding location
🤖

1. Add location via search
2. ✅ Location card visible with city name
3. ✅ Drag handle visible on location card

> Covered by SEARCH_S1 - should add location from search suggestion
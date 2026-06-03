# UI_CARD_SWIPE - SCENARIO: Swipe card to delete
## UI_CARD_SWIPE_S1 - should show delete confirmation dialog when swiped left
🤖

1. Add location card
2. Swipe card left
3. ✅ Delete confirmation dialog appears

## UI_CARD_SWIPE_S2 - should remove card when delete confirmed
🤖

1. Add location card
2. Swipe left → confirmation dialog
3. Tap confirm
4. ✅ Card removed, empty card appears

## UI_CARD_SWIPE_S3 - should keep card when delete dismissed
🤖

1. Add location card
2. Swipe left → confirmation dialog
3. Tap dismiss
4. ✅ Card still visible

## UI_CARD_SWIPE_S4 - should remove GPS card when swiped and confirmed
🤖

1. GPS card visible
2. Swipe left → confirmation dialog
3. Tap confirm
4. ✅ GPS card removed, empty card appears
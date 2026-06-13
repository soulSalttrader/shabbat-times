# UI_CARD_REORDER - SCENARIO: Reorder cards via drag handle
## UI_CARD_REORDER_S1 -  should persist new sort order after reorder
🖐️ (gesture not automatable)
🔧 Partially covered by REPO_REORDER_S1 (persistence logic)

1. Add two location cards (A, B)
2. Drag B above A
3. ✅ Order is B, A in repo

> ReorderableItem uses custom pointer input not triggerable
> via Compose test performTouchInput.
> Drag handle presence is verified automatically in UI_CARD_CONTENT_S5/S6.

> Drag gesture can't be automated via performTouchInput.
> Reorder persistence is verified at repository level.
> UI order change requires manual verification.

## UI_CARD_REORDER_S2 - should persist sort order when card moved down
🖐️ see UI_CARD_REORDER_S1

## UI_CARD_REORDER_S3 - should persist sort order when GPS card reordered
🖐️ see UI_CARD_REORDER_S1
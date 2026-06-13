# UI_CARD_REORDER - SCENARIO: Reorder cards via drag handle
## REPO_REORDER_S1 - should persist new sort order after reorder
🔧 (SavedLocationsRepositoryRoomTest)

1. Save [ID1, ID2, ID3]
2. Reorder to [ID1, ID3, ID2]
3. ✅ DAO returns [ID1, ID3, ID2] in correct order

## REPO_REORDER_S2 - should persist sort order when card moved down
🔧 (SavedLocationsRepositoryRoomTest)

1. Save [ID1, ID2, ID3]
2. Reorder to [ID3, ID2, ID1] → move ID1 down
3. ✅ DAO returns [ID3, ID2, ID1] with updated sortOrder

## REPO_REORDER_S3 - should persist sort order when GPS card reordered
🔧 (SavedLocationsRepositoryRoomTest)

1. Save [GPS, ID1, ID2, ID3]
2. Reorder to [ID1, ID2, GPS, ID3]
3. ✅ DAO returns [ID1, ID2, GPS, ID3] with correct sortOrder
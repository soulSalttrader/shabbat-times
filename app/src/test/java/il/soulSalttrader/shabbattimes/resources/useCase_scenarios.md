# Reorder Use Case

## USECASE_REORDER_S1 - should move first item to the end
📏

1. List: [ID1, ID2, ID3]
2. reorder(from=0, to=2)
3. ✅ Repository called with [ID2, ID3, ID1]

## USECASE_REORDER_S2 - should move item from last to first
📏

1. List: [ID1, ID2, ID3]
2. reorder(from=2, to=0)
3. ✅ Repository called with [ID3, ID1, ID2]

## USECASE_REORDER_S3 - should move item in middle of list
📏

1. List: [ID1, ID2, ID3]
2. reorder(from=2, to=0)
3. ✅ Repository called with [ID3, ID1, ID2]

## USECASE_REORDER_S4 - should move item to same position
📏

1. List: [ID1, ID2, ID3]
2. reorder(from=0, to=0)
3. ✅ Repository called with [ID1, ID2, ID3] (unchanged)

## USECASE_REORDER_S5 - should reorder GPS card
📏

1. List: [GPS, ID1, ID2]
2. reorder(from=0, to=2)
3. ✅ Repository called with [ID1, ID2, GPS]
## ⚠️ BUG_SEARCH_S1 — Location saved twice on suggestion select
🎨 (FakeSavedLocationsRepository)

> Exposed by fake repository which has no duplicate guard unlike Room.
> save() is called twice when suggestion is selected:
> - first call: save(Brno), current: []
> - second call: save(Brno), current: [Brno]
    > Production hidden by Room's OnConflictStrategy.IGNORE silently rejecting duplicate key.
    > Fake correctly reveals the real double-dispatch behavior.
    > Fix: find and remove duplicate save() call in SearchViewModel dispatch handling.
    > Related: SEARCH_ADD_LOCATION_S1

## ✅ UI_DIALOG_S2 - should show rationale dialog when system permission denied
## ✅ UI_DIALOG_S1 - should show system dialog after education dialog

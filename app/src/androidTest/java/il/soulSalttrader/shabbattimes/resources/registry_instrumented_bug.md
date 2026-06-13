## ✅ UI_SEARCH_S1 - should add location from search suggestion

> Location saved twice on suggestion select.
> Exposed by fake repository which has no duplicate guard unlike Room.
> save() is called twice when suggestion is selected:
> - first call: save(Brno), current: []
> - second call: save(Brno), current: [Brno]
    > Production hidden by Room's OnConflictStrategy.IGNORE silently rejecting duplicate key.
    > Fake correctly reveals the real double-dispatch behavior.
    > Fix: find and remove duplicate save() call in SearchViewModel dispatch handling.

## ✅ UI_DIALOG_S2 - should show rationale dialog when system permission denied
## ✅ UI_DIALOG_S1 - should show system dialog after education dialog

## ✅ PERM_DISPATCH_S1_1 - should dispatch AllGranted exactly once 
> AllGranted dispatched 3x after permission granted
> observed in logs
> event:AllGranted → HandlePermissions.kt:36  (LaunchedEffect result)
> event:AllGranted → HandlePermissions.kt:41  (LaunchedEffect Unit)
> event:AllGranted → HandlePermissions.kt:25  (LifecycleEventEffect ON_RESUME)
> 
> 
> > Root Cause
> `HandlePermissions` had multiple independent triggers firing for the same permission result:
> - `LifecycleEventEffect(ON_RESUME)` + `LaunchedEffect(permissionState.permission)` both resolved the same result
> - `LaunchedEffect(Unit)` + `LaunchedEffect(ON_RESUME)` duplicated the initial check

> Diagnosed via stack trace logging in `dispatch()`:
> ```kotlin
> val caller = Thread.currentThread().stackTrace
>     .drop(2).take(5)
>     .joinToString("\n") { "  at ${it.className}.${it.methodName}(${it.fileName}:${it.lineNumber})" }
> Log.d("PERM_DISPATCHER", "dispatch: event=$event\n$caller")
> ```

> Fix
> Removed redundant `LaunchedEffect(Unit)` and `LaunchedEffect(ON_RESUME)`.
> Guarded `LifecycleEventEffect(ON_RESUME)` to skip when an active request flow is in progress.

## ✅ PERM_DISPATCH_S1_2 - should dispatch DeniedWithRationale exactly once 
> DeniedWithRationale dispatched 2x after permission denied
> observed in logs
> event:DeniedWithRationale → HandlePermissions.kt:25  (LifecycleEventEffect ON_RESUME)
> event:DeniedWithRationale → HandlePermissions.kt:50  (LaunchedEffect result)
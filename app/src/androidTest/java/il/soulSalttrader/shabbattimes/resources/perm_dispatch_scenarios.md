# PERM_DISPATCH_S1 - SCENARIO: should never dispatch same event multiple times consecutively
## PERM_DISPATCH_S1_1 - should dispatch AllGranted exactly once
> Diagnosed via stack trace logging in `dispatch()`:
> ```kotlin
> val caller = Thread.currentThread().stackTrace
>     .drop(2).take(5)
>     .joinToString("\n") { "  at ${it.className}.${it.methodName}(${it.fileName}:${it.lineNumber})" }
> Log.d("PERM_DISPATCHER", "dispatch: event=$event\n$caller")
> ```

## PERM_DISPATCH_S1_2 - should dispatch DeniedWithRationale exactly once
> Diagnosed via stack trace logging in `dispatch()`:
> ```kotlin
> val caller = Thread.currentThread().stackTrace
>     .drop(2).take(5)
>     .joinToString("\n") { "  at ${it.className}.${it.methodName}(${it.fileName}:${it.lineNumber})" }
> Log.d("PERM_DISPATCHER", "dispatch: event=$event\n$caller")
> ```
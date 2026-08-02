package il.soulSalttrader.shabbattimes.ui.permission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import il.soulSalttrader.shabbattimes.permission.PermissionHandler
import il.soulSalttrader.shabbattimes.permission.PermissionHandlerImpl

/**
 * Overrides [rememberPermissionHandler] with a test double.
 * Provide a [PermissionHandler] via [CompositionLocalProvider] in tests to avoid
 * real permission launcher and context dependencies.
 *
 * Defaults to null — production code always uses the real [rememberPermissionHandler] impl.
 */
val LocalPermissionHandler = compositionLocalOf<PermissionHandler?> { null }

@Composable
fun rememberPermissionHandler(): PermissionHandler {
    // In tests, a fake handler can be injected via CompositionLocalProvider(LocalPermissionHandler provides fake).
    // Production always falls through to the real implementation below.
    LocalPermissionHandler.current?.let { return it }

    val context = LocalContext.current
    lateinit var handler: PermissionHandlerImpl

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { result -> handler.onResult(result) }

    fun checkPermission(perm: String) =
        ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED

    fun checkShouldShowRationale(perm: String) =
        ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, perm)

    handler = remember(context, launcher) {
        PermissionHandlerImpl(
            checkPermission = ::checkPermission,
            checkShouldShowRationale = ::checkShouldShowRationale,
            launch = launcher::launch,
        )
    }

    return handler
}

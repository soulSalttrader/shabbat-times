package il.soulSalttrader.shabbattimes.permission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun rememberPermissionHandler(): PermissionHandler {
    val context = LocalContext.current
    lateinit var handler: PermissionHandlerImpl

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result -> handler.onResult(result) }


    fun checkPermission(perm: String) =
        ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED

    fun checkShouldShowRationale(perm: String) =
        ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, perm)

    handler = remember(context, launcher) {
        PermissionHandlerImpl(
            checkPermission = ::checkPermission,
            checkShouldShowRationale = ::checkShouldShowRationale,
            launch = launcher::launch
        )
    }

    return handler
}
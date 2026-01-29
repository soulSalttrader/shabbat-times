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
    ) { result ->
        handler.onResult(result)
    }

    handler = remember(context, launcher) {
        PermissionHandlerImpl(
            isGranted = { perm ->
                ContextCompat.checkSelfPermission(
                    context,
                    perm
                ) == PackageManager.PERMISSION_GRANTED
            },
            shouldShowRationale = { perm ->
                val activity = context as Activity
                ActivityCompat.shouldShowRequestPermissionRationale(activity, perm)
            },
            launch = launcher::launch
        )
    }

    return handler
}
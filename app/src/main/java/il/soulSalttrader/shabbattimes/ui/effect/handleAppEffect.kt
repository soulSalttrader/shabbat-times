package il.soulSalttrader.shabbattimes.ui.effect

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import il.soulSalttrader.shabbattimes.common.openAppSettings

suspend fun handleAppEffect(
    effect: AppEffect,
    context: Context,
    snackbarHostState: SnackbarHostState,
) {
    when (effect) {
        is AppEffect.ShowToast -> {
            Toast.makeText(
                context,
                effect.message.resolve(context),
                Toast.LENGTH_LONG,
            ).show()
        }
        is AppEffect.ShowSnackBar -> {
            val result = snackbarHostState.showSnackbar(
                message = effect.message.resolve(context),
                actionLabel = effect.actionLabel?.resolve(context),
                duration = SnackbarDuration.Long,
            )

            if (result == SnackbarResult.ActionPerformed) { effect.onAction?.invoke() }
        }
        is AppEffect.OpenAppSettings -> context.openAppSettings()
    }
}
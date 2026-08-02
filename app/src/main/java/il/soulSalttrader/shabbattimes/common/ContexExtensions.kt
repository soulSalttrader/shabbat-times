package il.soulSalttrader.shabbattimes.common

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.text.format.DateFormat
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import il.soulSalttrader.shabbattimes.common.constants.DateTimeFormatters.TIME_12H
import il.soulSalttrader.shabbattimes.common.constants.DateTimeFormatters.TIME_24H
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun Context.openAppSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null),
    ).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Safe for non-Activity contexts
    }
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        startActivity(Intent(Settings.ACTION_SETTINGS))
    }
}

fun Context.openEmail(email: String, subject: String? = null) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:$email".toUri()
        subject?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
    }
    runCatching { startActivity(intent) }
}

fun Context.openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    runCatching { startActivity(intent) }
}

@Composable
fun Context.formatTime(
    localTime: LocalTime,
    amPmScale: Float = 0.65f,
    alpha: Float = 0.90f,
): AnnotatedString {
    val is24Hour = DateFormat.is24HourFormat(this)

    if (is24Hour) {
        return AnnotatedString(localTime.format(DateTimeFormatter.ofPattern(TIME_24H)))
    }

    val timeStr = localTime.format(DateTimeFormatter.ofPattern(TIME_12H))
    val amPmStart = timeStr.length - 2

    return buildAnnotatedString {
        append(timeStr.substring(0, amPmStart))

        withStyle(
            SpanStyle(
                fontSize = (LocalTextStyle.current.fontSize.value * amPmScale).sp,
                color = LocalContentColor.current.copy(alpha),
            ),
        ) {
            append(timeStr.substring(amPmStart))
        }
    }
}

fun Context.formatDate(date: LocalDate): String {
    val locale = this.resources.configuration.locales[0] ?: Locale.getDefault()

    return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(locale)
        .format(date)
}

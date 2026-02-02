package il.soulSalttrader.shabbattimes.model

import android.content.Context
import il.soulSalttrader.shabbattimes.common.toDisplayString

fun HalachicTimes.toDisplay(context: Context): HalachicTimesDisplay =
    HalachicTimesDisplay(
        city = city,
        candleLightingTime = candleLightingTime.toDisplayString(context),
        candleLightingDate = candleLightingDate.toDisplayString(),
        havdalahTime = havdalahTime.toDisplayString(context),
        havdalahDate = havdalahDate.toDisplayString(),
        locationStatus = locationStatus,
    )
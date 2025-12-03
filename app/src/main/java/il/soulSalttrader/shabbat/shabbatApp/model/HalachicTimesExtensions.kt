package il.soulSalttrader.retro.shabbatApp.model

import android.content.Context
import il.soulSalttrader.retro.shabbatApp.common.toDisplayString

fun HalachicTimes.toDisplay(context: Context): HalachicTimesDisplay =
    HalachicTimesDisplay(
        candleLightingTime = this.candleLightingTime.toDisplayString(context),
        candleLightingDate = this.candleLightingDate.toDisplayString(),
        havdalahTime = this.havdalahTime.toDisplayString(context),
        havdalahDate = this.havdalahDate.toDisplayString(),
    )
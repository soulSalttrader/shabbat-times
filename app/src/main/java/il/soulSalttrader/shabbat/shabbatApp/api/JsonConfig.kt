package il.soulSalttrader.retro.shabbatApp.api

import il.soulSalttrader.retro.core.Debug
import kotlinx.serialization.json.Json

object JsonConfig {
    val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
        prettyPrint = Debug.enabled
    }
}
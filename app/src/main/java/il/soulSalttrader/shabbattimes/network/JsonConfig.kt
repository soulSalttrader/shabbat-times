package il.soulSalttrader.shabbattimes.network

import il.soulSalttrader.shabbattimes.Debug
import kotlinx.serialization.json.Json

object JsonConfig {
    val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
        prettyPrint = Debug.enabled
    }
}
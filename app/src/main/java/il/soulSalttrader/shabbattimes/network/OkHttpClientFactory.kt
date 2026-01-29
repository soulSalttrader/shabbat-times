package il.soulSalttrader.shabbattimes.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object OkHttpClientFactory {
    fun create(isDebug: Boolean): OkHttpClient = OkHttpClient.Builder().apply {
        if (isDebug) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            addInterceptor(logging)
        }
    }.build()
}
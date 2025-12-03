package il.soulSalttrader.retro.shabbatApp.settings

import android.content.Context
import android.text.format.DateFormat
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    fun is24HourFormat(): Boolean = DateFormat.is24HourFormat(context)
}
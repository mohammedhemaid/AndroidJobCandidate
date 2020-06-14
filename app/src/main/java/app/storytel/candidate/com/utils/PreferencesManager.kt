package app.storytel.candidate.com.utils

import android.content.Context
import android.content.SharedPreferences
import app.storytel.candidate.com.theme.ThemeMode

private const val PREFS_KEY = "app.storytel.candidate"
private const val THEME_MODE = "themeMode"

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

    @ThemeMode var themeMode: Int
        get() = prefs.getInt(THEME_MODE, ThemeMode.FOLLOW_SYSTEM)
        set(value) {
            prefs.edit().putInt(THEME_MODE, value).apply()
        }
}
package app.storytel.candidate.com.init

import android.app.Application
import app.storytel.candidate.com.theme.ThemeManager
import app.storytel.candidate.com.utils.PreferencesManager

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val preferencesManager = PreferencesManager(this)
        ThemeManager.applyTheme(preferencesManager.themeMode)
    }
}
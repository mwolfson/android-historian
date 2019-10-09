package com.designdemo.uaha

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import com.designdemo.uaha.util.PREF_DARK_MODE
import com.designdemo.uaha.util.PREF_FILE

class HistorianApplication : Application() {

    companion object {
        private const val TAG = "HistorianApplication"
    }

    override fun onCreate() {
        super.onCreate()

        // The default value is different after Android Q
        val defaultNightMode: Int
        when (Build.VERSION.SDK_INT) {
            in Int.MIN_VALUE..Build.VERSION_CODES.P -> defaultNightMode = MODE_NIGHT_AUTO_BATTERY
            else -> defaultNightMode = MODE_NIGHT_FOLLOW_SYSTEM
        }

        // If the user has previously set a value for darkMode, use that
        val sharedPref = applicationContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE) ?: return
        val darkMode = sharedPref.getInt(PREF_DARK_MODE, defaultNightMode)
        Log.d(TAG, "MSW Historian Value $darkMode and the current value for Build.VERSION.SDK_INT is ${Build.VERSION.SDK_INT}")
        AppCompatDelegate.setDefaultNightMode(darkMode)
    }
}
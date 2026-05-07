package com.manekelsa.app.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * PreferencesManager — stores user preferences like language selection
 */
class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "mane_kelsa_prefs",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_LANGUAGE = "selected_language"
        private const val KEY_FIRST_LAUNCH = "is_first_launch"
    }

    var selectedLanguage: String
        get() = prefs.getString(KEY_LANGUAGE, "en") ?: "en"
        set(value) = prefs.edit { putString(KEY_LANGUAGE, value) }

    var isFirstLaunch: Boolean
        get() = prefs.getBoolean(KEY_FIRST_LAUNCH, true)
        set(value) = prefs.edit { putBoolean(KEY_FIRST_LAUNCH, value) }
}

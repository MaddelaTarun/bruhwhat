package com.manekelsa.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.manekelsa.app.data.PreferencesManager
import com.manekelsa.app.navigation.ManeKelsaNavGraph
import com.manekelsa.app.navigation.Screen
import com.manekelsa.app.ui.theme.ManeKelsaTheme
import java.util.Locale

/**
 * MainActivity — single-activity entry point with language selection support.
 * All navigation is handled by Jetpack Compose NavHost.
 */
class MainActivity : ComponentActivity() {

    private lateinit var prefsManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        prefsManager = PreferencesManager(this)

        // Apply saved language
        setAppLocale(this, prefsManager.selectedLanguage)

        setContent {
            ManeKelsaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var currentLanguage by remember { mutableStateOf(prefsManager.selectedLanguage) }

                    // DEBUG: Uncomment to force language selection screen
                    // prefsManager.isFirstLaunch = true

                    val startDestination = if (prefsManager.isFirstLaunch) {
                        Screen.LanguageSelection.route
                    } else {
                        Screen.Feed.route
                    }

                    ManeKelsaNavGraph(
                        startDestination = startDestination,
                        onLanguageSelected = { languageCode ->
                            prefsManager.selectedLanguage = languageCode
                            prefsManager.isFirstLaunch = false
                            currentLanguage = languageCode
                            setAppLocale(this@MainActivity, languageCode)
                            // Recreate activity to apply new locale
                            recreate()
                        }
                    )
                }
            }
        }

        // UNCOMMENT BELOW TO SEED TEST DATA (run once, then comment out again)
        // com.manekelsa.app.data.FirebaseRepository().seedMockData()
    }

    private fun setAppLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}

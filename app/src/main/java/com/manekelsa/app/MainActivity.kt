package com.manekelsa.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.manekelsa.app.navigation.ManeKelsaNavGraph
import com.manekelsa.app.ui.theme.ManeKelsaTheme

/**
 * MainActivity — single-activity entry point.
 * All navigation is handled by Jetpack Compose NavHost.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ManeKelsaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ManeKelsaNavGraph()
                }
            }
        }

        // TODO: Remove after first run — seeds mock data
        com.manekelsa.app.data.FirebaseRepository().seedMockData()
    }
}

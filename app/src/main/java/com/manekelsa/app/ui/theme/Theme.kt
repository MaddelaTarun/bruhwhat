package com.manekelsa.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ─── High-contrast color palette for semi-literate users ─────────────────────

val DeepSaffron = Color(0xFFFF6B00)       // Primary — warm, energetic
val DarkSaffron = Color(0xFFCC5500)       // Primary variant
val LightSaffron = Color(0xFFFFE0CC)      // Primary container

val ForestGreen = Color(0xFF1B7A3E)       // Secondary — nature / availability
val LightGreen = Color(0xFFB7F0CC)        // Secondary container

val OffWhite = Color(0xFFFFF8F2)          // Background
val SurfaceWhite = Color(0xFFFFFFFF)      // Surface
val DarkText = Color(0xFF1A1A1A)          // On-surface text
val MediumGray = Color(0xFF6B6B6B)        // Subtitle text
val ErrorRed = Color(0xFFD32F2F)          // Error

val AvailableGreen = Color(0xFF2E7D32)
val UnavailableGray = Color(0xFF9E9E9E)

private val LightColorScheme = lightColorScheme(
    primary = DeepSaffron,
    onPrimary = Color.White,
    primaryContainer = LightSaffron,
    onPrimaryContainer = DarkSaffron,
    secondary = ForestGreen,
    onSecondary = Color.White,
    secondaryContainer = LightGreen,
    onSecondaryContainer = ForestGreen,
    background = OffWhite,
    onBackground = DarkText,
    surface = SurfaceWhite,
    onSurface = DarkText,
    surfaceVariant = Color(0xFFF5EDE6),
    onSurfaceVariant = MediumGray,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun ManeKelsaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = ManeKelsaTypography,
        content = content
    )
}

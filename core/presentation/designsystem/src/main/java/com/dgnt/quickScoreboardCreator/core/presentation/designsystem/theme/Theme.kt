package com.dgnt.quickScoreboardCreator.core.presentation.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9ecafc),
    onPrimary = Color(0xFF003256),
    primaryContainer = Color(0xFF154974),
    onPrimaryContainer = Color(0xFFd0e4ff),
    inversePrimary = Color(0xFF33618d),
    secondary = Color(0xFFbac8db),
    onSecondary = Color(0xFF243140),
    secondaryContainer = Color(0xFF3b4857),
    onSecondaryContainer = Color(0xFFd6e4f7),
    tertiary = Color(0xFFd5bee5),
    onTertiary = Color(0xFF3a2a48),
    tertiaryContainer = Color(0xFF514060),
    onTertiaryContainer = Color(0xFFf1dbff),
//    background = ColorLightTokens.Background,
//    onBackground = ColorLightTokens.OnBackground,
    surface = Color(0xFF101418),
    onSurface = Color(0xFFe0e2e8),
//    surfaceVariant = ColorLightTokens.SurfaceVariant,
    onSurfaceVariant = Color(0xFFc2c7cf),
//    surfaceTint = primary,
    inverseSurface = Color(0xFFe0e2e8),
    inverseOnSurface = Color(0xFF2d3135),
    error = Color(0xFFffb4ab),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000a),
    onErrorContainer = Color(0xFFffdad6),
    outline = Color(0xFF8c9199),
    outlineVariant = Color(0xFF42474e),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFF36393e),
    surfaceContainer = Color(0xFF1d2024),
    surfaceContainerHigh = Color(0xFF272a2f),
    surfaceContainerHighest = Color(0xFF32353a),
    surfaceContainerLow = Color(0xFF191c20),
    surfaceContainerLowest = Color(0xFF0b0e12),
    surfaceDim = Color(0xFF101418),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF33618d),
    onPrimary = Color(0xFFffffff),
    primaryContainer = Color(0xFFd0e4ff),
    onPrimaryContainer = Color(0xFF001d35),
    inversePrimary = Color(0xFF9ecafc),
    secondary = Color(0xFF526070),
    onSecondary = Color(0xFFffffff),
    secondaryContainer = Color(0xFFd6e4f7),
    onSecondaryContainer = Color(0xFF0f1d2a),
    tertiary = Color(0xFF6a5779),
    onTertiary = Color(0xFFffffff),
    tertiaryContainer = Color(0xFFf1dbff),
    onTertiaryContainer = Color(0xFF241532),
//    background = ColorLightTokens.Background,
//    onBackground = ColorLightTokens.OnBackground,
    surface = Color(0xFFf8f9ff),
    onSurface = Color(0xFF191c20),
//    surfaceVariant = ColorLightTokens.SurfaceVariant,
    onSurfaceVariant = Color(0xFF42474e),
//    surfaceTint = primary,
    inverseSurface = Color(0xFF2d3135),
    inverseOnSurface = Color(0xFFeff0f7),
    error = Color(0xFFba1a1a),
    onError = Color(0xFFffffff),
    errorContainer = Color(0xFFffdad6),
    onErrorContainer = Color(0xFF410002),
    outline = Color(0xFF73777f),
    outlineVariant = Color(0xFFc2c7cf),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFFf8f9ff),
    surfaceContainer = Color(0xFFeceef4),
    surfaceContainerHigh = Color(0xFFe6e8ee),
    surfaceContainerHighest = Color(0xFFe0e2e8),
    surfaceContainerLow = Color(0xFFf2f3f9),
    surfaceContainerLowest = Color(0xFFffffff),
    surfaceDim = Color(0xFFd8dae0),
)

@Composable
fun QuickScoreboardCreatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
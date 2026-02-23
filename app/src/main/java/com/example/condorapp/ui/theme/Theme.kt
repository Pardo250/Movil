package com.example.condorapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta Modo Claro
private val LightColorScheme = lightColorScheme(
    primary = CondorGreen,
    onPrimary = Color.White,
    secondary = CondorBrown,
    onSecondary = Color.White,
    background = CondorBackground,
    surface = CondorWhite,
    onSurface = Color.Black,
    surfaceVariant = CondorSurfaceBeige,
    outline = CondorGray,
    error = CondorRed
)

// Paleta Modo Oscuro (Colores ajustados para confort visual)
private val DarkColorScheme = darkColorScheme(
    primary = CondorSoftGreen, // Verde más claro para resaltar en fondo oscuro
    onPrimary = Color.Black,
    secondary = CondorBrown,
    background = Color(0xFF1A1C18), // Fondo casi negro oliva
    surface = Color(0xFF2E312C),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF44483D),
    outline = CondorSoftGreen,
    error = Color(0xFFFFB4AB)
)

@Composable
fun CondorappTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

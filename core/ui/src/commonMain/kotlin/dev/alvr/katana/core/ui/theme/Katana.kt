package dev.alvr.katana.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.PaletteStyle.Vibrant
import com.materialkolor.dynamicColorScheme

@Composable
fun KatanaTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = dynamicColorScheme(
            seedColor = Color(ColorSeed),
            isDark = isSystemInDarkTheme(),
            style = Vibrant,
        ),
        typography = KatanaTypography,
        content = content,
    )
}

private const val ColorSeed = 0xFF333941 // Same as Android's windowSplashScreenBackground attr

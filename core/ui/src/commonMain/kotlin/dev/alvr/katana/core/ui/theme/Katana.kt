package dev.alvr.katana.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle.Vibrant
import com.materialkolor.rememberDynamicMaterialThemeState

@Composable
fun KatanaTheme(
    content: @Composable () -> Unit,
) {
    DynamicMaterialTheme(
        state = rememberDynamicMaterialThemeState(
            seedColor = Color(ColorSeed),
            isDark = isSystemInDarkTheme(),
            style = Vibrant,
        ),
        animate = true,
        typography = KatanaTypography,
        content = content,
    )
}

private const val ColorSeed = 0xFF333941 // Same as Android's windowSplashScreenBackground attr

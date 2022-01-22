package dev.alvr.katana.ui.base.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import dev.alvr.katana.ui.base.colors.palettes.KatanaDarkPalette
import dev.alvr.katana.ui.base.colors.palettes.KatanaLightPalette
import dev.alvr.katana.ui.base.shapes.KatanaShapes
import dev.alvr.katana.ui.base.typography.KatanaTypography

@Composable
fun KatanaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        KatanaDarkPalette
    } else {
        KatanaLightPalette
    }

    MaterialTheme(
        colors = colors,
        typography = KatanaTypography,
        shapes = KatanaShapes,
        content = content
    )
}

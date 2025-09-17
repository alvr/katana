package dev.alvr.katana.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamiccolor.ColorSpec
import com.materialkolor.rememberDynamicMaterialThemeState

@Composable
fun KatanaTheme(
    content: @Composable () -> Unit,
) {
    // TODO: Restore when stable version of M3 Expressive is released
    // DynamicMaterialExpressiveTheme
    DynamicMaterialTheme(
        state = rememberDynamicMaterialThemeState(
            seedColor = Color(ColorSeed),
            isDark = true,
            specVersion = ColorSpec.SpecVersion.SPEC_2025,
            style = PaletteStyle.Expressive,
        ),
        animate = true,
        typography = KatanaTypography,
        content = content,
    )
}

object KatanaTheme {
    val alpha: Alpha
        @Composable
        @ReadOnlyComposable
        get() = LocalAlpha.current

    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    val dimensions: Dimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalDimensions.current

    // TODO: Restore when stable version of M3 Expressive is released
    // @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    // val motionScheme
    //     @Composable
    //     @ReadOnlyComposable
    //     get() = MaterialTheme.motionScheme

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    val sizes: Sizes
        @Composable
        @ReadOnlyComposable
        get() = LocalSizes.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography
}

private const val ColorSeed = 0xFF333941 // Same as Android's windowSplashScreenBackground attr

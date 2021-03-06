package dev.alvr.katana.ui.base

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.alvr.katana.ui.base.design.KatanaDarkTheme
import dev.alvr.katana.ui.base.design.KatanaShapes
import dev.alvr.katana.ui.base.design.KatanaTypography

@Composable
internal fun KatanaTheme(
    content: @Composable () -> Unit,
) {
    val colors = KatanaDarkTheme
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(color = colors.surface, darkIcons = colors.isLight)
    }

    MaterialTheme(
        colors = colors,
        typography = KatanaTypography,
        shapes = KatanaShapes,
        content = content,
    )
}

package dev.alvr.katana.ui.base

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import dev.alvr.katana.ui.base.design.KatanaDarkTheme
import dev.alvr.katana.ui.base.design.KatanaShapes
import dev.alvr.katana.ui.base.design.KatanaTypography

@Composable
internal fun KatanaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = KatanaDarkTheme,
        typography = KatanaTypography,
        shapes = KatanaShapes,
        content = content
    )
}

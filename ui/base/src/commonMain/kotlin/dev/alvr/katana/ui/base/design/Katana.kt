package dev.alvr.katana.ui.base.design

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun KatanaTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = KatanaDarkTheme,
        typography = KatanaTypography,
        shapes = KatanaShapes,
        content = content,
    )
}

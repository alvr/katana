package dev.alvr.katana.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions internal constructor(
    val spacing1: Dp = 4.dp,
    val spacing2: Dp = 8.dp,
    val spacing3: Dp = 12.dp,
    val spacing4: Dp = 16.dp,
    val spacing5: Dp = 20.dp,
    val spacing6: Dp = 24.dp,
    val spacing8: Dp = 32.dp,
    val spacing10: Dp = 40.dp,
    val spacing12: Dp = 48.dp,
    val spacing16: Dp = 64.dp,
    val spacing32: Dp = 128.dp,

    val contentPaddingSmall: Dp = spacing2,
    val contentPaddingMedium: Dp = spacing3,
    val contentPaddingLarge: Dp = spacing4,

    val itemSpacing: Dp = spacing2,
    val pageSpacing: Dp = spacing3,
)

internal val LocalDimensions = staticCompositionLocalOf { Dimensions() }

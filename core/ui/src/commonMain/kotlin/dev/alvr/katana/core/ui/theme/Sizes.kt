package dev.alvr.katana.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Sizes internal constructor(
    val cardWidth: Dp = 320.dp,
    val coverWidth: Dp = 96.dp,
)

internal val LocalSizes = staticCompositionLocalOf { Sizes() }

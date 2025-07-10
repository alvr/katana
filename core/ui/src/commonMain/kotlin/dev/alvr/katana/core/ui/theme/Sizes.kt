package dev.alvr.katana.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
@Suppress("UseDataClass")
class Sizes internal constructor(
    val size1: Dp = 4.dp,
    val size2: Dp = 8.dp,
    val size3: Dp = 12.dp,
    val size4: Dp = 16.dp,
    val size5: Dp = 20.dp,
    val size6: Dp = 24.dp,
    val size8: Dp = 32.dp,
    val size10: Dp = 40.dp,
    val size12: Dp = 48.dp,
    val size16: Dp = 64.dp,
    val size18: Dp = 72.dp,
    val size32: Dp = 128.dp,
    val size40: Dp = 160.dp,
    val size48: Dp = 192.dp,
    val size64: Dp = 256.dp,

    val cardHeight: Dp = 144.dp,
    val cardWidth: Dp = 320.dp,
    val coverWidth: Dp = 96.dp,
    val lastItemListHeight: Dp = 72.dp, // 56.dp FAB + 16.dp spacing bottom
)

internal val LocalSizes = staticCompositionLocalOf { Sizes() }

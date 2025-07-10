package dev.alvr.katana.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
@Suppress("UseDataClass")
class Alpha internal constructor(
    val alpha0: Float = 0f,
    val alpha15: Float = 0.15f,
    val alpha25: Float = 0.25f,
    val alpha33: Float = 0.33f,
    val alpha50: Float = 0.5f,
    val alpha66: Float = 0.66f,
    val alpha75: Float = 0.75f,
    val alpha85: Float = 0.85f,
    val alpha100: Float = 1f,
)

internal val LocalAlpha = staticCompositionLocalOf { Alpha() }

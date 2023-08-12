package dev.alvr.katana.ui.base.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

expect fun Modifier.katanaPlaceholder(
    visible: Boolean,
    shape: Shape? = null,
): Modifier

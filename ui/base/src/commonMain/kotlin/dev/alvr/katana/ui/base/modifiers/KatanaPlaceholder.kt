package dev.alvr.katana.ui.base.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Shape
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.material.placeholder
import com.eygraber.compose.placeholder.material.shimmer

fun Modifier.katanaPlaceholder(
    visible: Boolean,
    shape: Shape? = null,
): Modifier = composed {
    placeholder(
        visible = visible,
        shape = shape,
        highlight = PlaceholderHighlight.shimmer(),
    )
}

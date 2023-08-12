package dev.alvr.katana.ui.base.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Shape
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

actual fun Modifier.katanaPlaceholder(
    visible: Boolean,
    shape: Shape?,
) = composed {
    placeholder(
        visible = visible,
        shape = shape,
        highlight = PlaceholderHighlight.shimmer(),
    )
}

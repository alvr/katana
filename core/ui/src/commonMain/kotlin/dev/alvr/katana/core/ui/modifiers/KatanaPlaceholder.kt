package dev.alvr.katana.core.ui.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.eygraber.compose.placeholder.PlaceholderDefaults
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.material3.placeholder
import com.eygraber.compose.placeholder.material3.shimmerHighlightColor
import com.eygraber.compose.placeholder.shimmer

@Composable
@Suppress("ModifierComposable")
fun Modifier.katanaPlaceholder(
    visible: Boolean,
    shape: Shape? = null,
): Modifier = placeholder(
    visible = visible,
    shape = shape,
    highlight = PlaceholderHighlight.shimmer(
        highlightColor = PlaceholderDefaults.shimmerHighlightColor(
            alpha = 0.33f,
        ),
    ),
)

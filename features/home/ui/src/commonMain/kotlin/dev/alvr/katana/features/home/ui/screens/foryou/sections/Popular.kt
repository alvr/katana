package dev.alvr.katana.features.home.ui.screens.foryou.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.home.ui.resources.Res
import dev.alvr.katana.features.home.ui.resources.popular_icon_button_a11y
import dev.alvr.katana.features.home.ui.resources.popular_title
import dev.alvr.katana.features.home.ui.screens.foryou.components.Lists
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouIntent

/**
 * Renders the "Popular" section and emits a navigation intent when the section's navigate action is clicked.
 *
 * @param onIntent Callback invoked with a [ForYouIntent]; called with `ForYouIntent.NavigateToPopular` when the navigate action is triggered.
 * @param modifier Optional [Modifier] for layout or styling.
 */
@Composable
internal fun Popular(onIntent: (ForYouIntent) -> Unit, modifier: Modifier = Modifier) {
    Lists(
        modifier = modifier,
        title = Res.string.popular_title.value,
        iconButtonContentDescription = Res.string.popular_icon_button_a11y.value,
        onNavigateClick = { onIntent(ForYouIntent.NavigateToPopular) },
    )
}

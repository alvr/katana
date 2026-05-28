package dev.alvr.katana.features.home.ui.screens.foryou.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.home.ui.resources.Res
import dev.alvr.katana.features.home.ui.resources.trending_icon_button_a11y
import dev.alvr.katana.features.home.ui.resources.trending_title
import dev.alvr.katana.features.home.ui.screens.foryou.components.Lists
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouIntent

/**
 * Renders a "Trending" section by delegating to the `Lists` composable.
 *
 * Shows the trending title and navigation icon; when the navigation button is activated,
 * invokes `onIntent` with `ForYouIntent.NavigateToTrending`.
 *
 * @param onIntent Callback invoked with user/navigation intents; called with `ForYouIntent.NavigateToTrending`
 *                 when the section's navigation button is clicked.
 * @param modifier Modifier to apply to the section's layout.
 */
@Composable
internal fun Trending(onIntent: (ForYouIntent) -> Unit, modifier: Modifier = Modifier) {
    Lists(
        modifier = modifier,
        title = Res.string.trending_title.value,
        iconButtonContentDescription = Res.string.trending_icon_button_a11y.value,
        onNavigateClick = { onIntent(ForYouIntent.NavigateToTrending) },
    )
}

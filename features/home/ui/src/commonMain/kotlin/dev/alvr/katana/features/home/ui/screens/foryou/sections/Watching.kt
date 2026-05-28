package dev.alvr.katana.features.home.ui.screens.foryou.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.home.ui.resources.Res
import dev.alvr.katana.features.home.ui.resources.watching_icon_button_a11y
import dev.alvr.katana.features.home.ui.resources.watching_title
import dev.alvr.katana.features.home.ui.screens.foryou.components.Lists
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouIntent

/**
 * Renders the "Watching" section and delegates its UI to the shared `Lists` component.
 *
 * @param onIntent Callback invoked to emit `ForYouIntent` actions; called with
 * `ForYouIntent.NavigateToAnimeLists` when the section's navigate action is triggered.
 * @param modifier Modifier applied to the `Lists` composable.
 */
@Composable
internal fun Watching(onIntent: (ForYouIntent) -> Unit, modifier: Modifier = Modifier) {
    Lists(
        modifier = modifier,
        title = Res.string.watching_title.value,
        iconButtonContentDescription = Res.string.watching_icon_button_a11y.value,
        onNavigateClick = { onIntent(ForYouIntent.NavigateToAnimeLists) },
    )
}

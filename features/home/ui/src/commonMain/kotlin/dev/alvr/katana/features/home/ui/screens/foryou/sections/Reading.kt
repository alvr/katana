package dev.alvr.katana.features.home.ui.screens.foryou.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.home.ui.resources.Res
import dev.alvr.katana.features.home.ui.resources.reading_icon_button_a11y
import dev.alvr.katana.features.home.ui.resources.reading_title
import dev.alvr.katana.features.home.ui.screens.foryou.components.Lists
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouIntent

/**
 * Renders the "Reading" section using the Lists component and wires its navigation action to the provided intent callback.
 *
 * @param onIntent Callback invoked with a `ForYouIntent`; tapping the navigation control sends `ForYouIntent.NavigateToMangaLists`.
 * @param modifier Modifier applied to the root composable.
 */
@Composable
internal fun Reading(onIntent: (ForYouIntent) -> Unit, modifier: Modifier = Modifier) {
    Lists(
        modifier = modifier,
        title = Res.string.reading_title.value,
        iconButtonContentDescription = Res.string.reading_icon_button_a11y.value,
        onNavigateClick = { onIntent(ForYouIntent.NavigateToMangaLists) },
    )
}

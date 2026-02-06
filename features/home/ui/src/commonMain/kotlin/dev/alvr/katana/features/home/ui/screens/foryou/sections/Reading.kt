package dev.alvr.katana.features.home.ui.screens.foryou.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.home.ui.resources.Res
import dev.alvr.katana.features.home.ui.resources.reading_icon_button_a11y
import dev.alvr.katana.features.home.ui.resources.reading_title
import dev.alvr.katana.features.home.ui.screens.foryou.components.Lists
import dev.alvr.katana.features.home.ui.viewmodel.HomeIntent

@Composable
internal fun Reading(onIntent: (HomeIntent) -> Unit, modifier: Modifier = Modifier) {
    Lists(
        modifier = modifier,
        title = Res.string.reading_title.value,
        iconButtonContentDescription = Res.string.reading_icon_button_a11y.value,
        onNavigateClick = { onIntent(HomeIntent.ForYouIntent.NavigateToMangaLists) },
    )
}

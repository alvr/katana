package dev.alvr.katana.features.home.ui.screens.foryou.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.home.ui.resources.Res
import dev.alvr.katana.features.home.ui.resources.watching_icon_button_a11y
import dev.alvr.katana.features.home.ui.resources.watching_title
import dev.alvr.katana.features.home.ui.screens.foryou.components.Lists
import dev.alvr.katana.features.home.ui.viewmodel.HomeIntent

@Composable
internal fun Watching(onIntent: (HomeIntent) -> Unit, modifier: Modifier = Modifier) {
    Lists(
        modifier = modifier,
        title = Res.string.watching_title.value,
        iconButtonContentDescription = Res.string.watching_icon_button_a11y.value,
        onNavigateClick = { onIntent(HomeIntent.ForYouIntent.NavigateToAnimeLists) },
    )
}

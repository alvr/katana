package dev.alvr.katana.features.home.ui.screens.foryou.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.home.ui.resources.Res
import dev.alvr.katana.features.home.ui.resources.trending_icon_button_a11y
import dev.alvr.katana.features.home.ui.resources.trending_title
import dev.alvr.katana.features.home.ui.screens.foryou.components.Lists
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouIntent

@Composable
internal fun Trending(onIntent: (ForYouIntent) -> Unit, modifier: Modifier = Modifier) {
    Lists(
        modifier = modifier,
        title = Res.string.trending_title.value,
        iconButtonContentDescription = Res.string.trending_icon_button_a11y.value,
        onNavigateClick = { onIntent(ForYouIntent.NavigateToTrending) },
    )
}

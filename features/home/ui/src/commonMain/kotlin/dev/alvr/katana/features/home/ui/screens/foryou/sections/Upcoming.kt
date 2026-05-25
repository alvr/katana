package dev.alvr.katana.features.home.ui.screens.foryou.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.home.ui.resources.Res
import dev.alvr.katana.features.home.ui.resources.upcoming_icon_button_a11y
import dev.alvr.katana.features.home.ui.resources.upcoming_title
import dev.alvr.katana.features.home.ui.screens.foryou.components.Lists
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouIntent

@Composable
internal fun Upcoming(onIntent: (ForYouIntent) -> Unit, modifier: Modifier = Modifier) {
    Lists(
        modifier = modifier,
        title = Res.string.upcoming_title.value,
        iconButtonContentDescription = Res.string.upcoming_icon_button_a11y.value,
        onNavigateClick = { onIntent(ForYouIntent.NavigateToUpcoming) },
    )
}

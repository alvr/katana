package dev.alvr.katana.features.home.ui.screens.foryou.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.viewmodel.SectionStatus
import dev.alvr.katana.features.home.ui.resources.Res
import dev.alvr.katana.features.home.ui.resources.watching_icon_button_a11y
import dev.alvr.katana.features.home.ui.resources.watching_title
import dev.alvr.katana.features.home.ui.screens.foryou.components.Lists
import dev.alvr.katana.features.home.ui.screens.foryou.entities.HomeMediaItem
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouIntent
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun Watching(
    status: SectionStatus<ImmutableList<HomeMediaItem>>,
    onIntent: (ForYouIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Lists(
        modifier = modifier,
        title = Res.string.watching_title.value,
        status = status,
        iconButtonContentDescription = Res.string.watching_icon_button_a11y.value,
        onNavigateClick = { onIntent(ForYouIntent.NavigateToAnimeLists) },
        onRetry = { onIntent(ForYouIntent.RetryWatching) },
    )
}

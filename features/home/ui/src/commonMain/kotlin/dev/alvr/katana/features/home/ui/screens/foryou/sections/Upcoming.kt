package dev.alvr.katana.features.home.ui.screens.foryou.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.viewmodel.SectionStatus
import dev.alvr.katana.features.home.ui.resources.Res
import dev.alvr.katana.features.home.ui.resources.upcoming_title
import dev.alvr.katana.features.home.ui.screens.foryou.components.Lists
import dev.alvr.katana.features.home.ui.screens.foryou.entities.HomeMediaItem
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouIntent
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun Upcoming(
    status: SectionStatus<ImmutableList<HomeMediaItem>>,
    onIntent: (ForYouIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Lists(
        modifier = modifier,
        title = Res.string.upcoming_title.value,
        status = status,
        onRetry = { onIntent(ForYouIntent.RetryUpcoming) },
    )
}

package dev.alvr.katana.features.home.ui.screens.foryou.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.home.ui.resources.Res
import dev.alvr.katana.features.home.ui.resources.trending_title
import dev.alvr.katana.features.home.ui.screens.foryou.components.Lists
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouIntent
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.SelectableMediaSectionState

@Composable
internal fun Trending(
    state: SelectableMediaSectionState,
    onIntent: (ForYouIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Lists(
        modifier = modifier,
        title = Res.string.trending_title.value,
        status = state.status,
        selectedType = state.selectedType,
        onTypeSelect = { type -> onIntent(ForYouIntent.SelectTrendingType(type)) },
        onRetry = { onIntent(ForYouIntent.RetryTrending) },
    )
}

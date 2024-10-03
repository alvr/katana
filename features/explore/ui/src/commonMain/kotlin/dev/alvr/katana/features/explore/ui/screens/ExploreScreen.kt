package dev.alvr.katana.features.explore.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.alvr.katana.core.ui.components.home.KatanaHomeScaffold
import dev.alvr.katana.core.ui.destinations.HomeDestination
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.explore.ui.navigation.ExploreNavigator
import dev.alvr.katana.features.explore.ui.resources.Res
import dev.alvr.katana.features.explore.ui.resources.explore_toolbar_search_placeholder
import dev.alvr.katana.features.explore.ui.resources.explore_toolbar_title
import dev.alvr.katana.features.explore.ui.viewmodel.ExploreViewModel
import org.koin.compose.viewmodel.koinViewModel



@Composable
@Suppress("UNUSED_PARAMETER")
internal fun ExploreScreen(
    navigator: ExploreNavigator,
    viewModel: ExploreViewModel = koinViewModel(),
) {
    KatanaHomeScaffold(
        title = Res.string.explore_toolbar_title.value,
        searchPlaceholder = Res.string.explore_toolbar_search_placeholder.value,
        onSearch = {},
        backContent = { Filter() },
    ) { paddingValues ->
        Text(
            modifier = Modifier.padding(paddingValues),
            text = Res.string.explore_toolbar_title.value,
        )
    }
}

@Composable
private fun Filter() {
    Text(text = "Explore Filter")
}

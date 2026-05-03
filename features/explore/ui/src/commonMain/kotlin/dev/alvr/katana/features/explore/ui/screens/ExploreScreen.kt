package dev.alvr.katana.features.explore.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.components.home.KatanaHomeScaffold
import dev.alvr.katana.core.ui.navigation.KatanaEntryProviderInstaller
import dev.alvr.katana.core.ui.navigation.destinations.MainDestination
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.explore.ui.resources.Res
import dev.alvr.katana.features.explore.ui.resources.explore_toolbar_search_placeholder
import dev.alvr.katana.features.explore.ui.resources.explore_toolbar_title
import dev.alvr.katana.features.explore.ui.viewmodel.ExploreViewModel
import dev.zacsweers.metrox.viewmodel.metroViewModel

internal fun explore(): KatanaEntryProviderInstaller = { entry<MainDestination.Explore> { ExploreScreen() } }

@Composable
@Suppress("UNUSED_PARAMETER")
internal fun ExploreScreen(viewModel: ExploreViewModel = metroViewModel()) {
    KatanaHomeScaffold(
        title = Res.string.explore_toolbar_title.value,
        searchPlaceholder = Res.string.explore_toolbar_search_placeholder.value,
        onSearch = {},
    ) { paddingValues ->
        Text(modifier = Modifier.padding(paddingValues), text = Res.string.explore_toolbar_title.value)
    }
}

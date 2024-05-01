package dev.alvr.katana.features.explore.ui.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.components.home.KatanaHomeScaffold
import dev.alvr.katana.core.ui.navigation.Destination
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.explore.ui.resources.Res
import dev.alvr.katana.features.explore.ui.resources.explore_toolbar_search_placeholder
import dev.alvr.katana.features.explore.ui.resources.explore_toolbar_title

@Composable
@Destination
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
internal fun ExploreScreen() {
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

package dev.alvr.katana.features.explore.ui.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.components.home.KatanaHomeScaffold
import dev.alvr.katana.core.ui.navigation.Destination
import dev.alvr.katana.features.explore.ui.strings.LocalExploreStrings

@Composable
@Destination
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
internal fun ExploreScreen() {
    val strings = LocalExploreStrings.current

    KatanaHomeScaffold(
        title = strings.exploreToolbarTitle,
        searchPlaceholder = strings.exploreToolbarSearchPlaceholder,
        onSearch = {},
        backContent = { Filter() },
    ) { paddingValues ->
        Text(
            modifier = Modifier.padding(paddingValues),
            text = strings.exploreToolbarTitle,
        )
    }
}

@Composable
private fun Filter() {
    Text(text = "Explore Filter")
}

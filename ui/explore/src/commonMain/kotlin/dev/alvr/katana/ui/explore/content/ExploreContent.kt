package dev.alvr.katana.ui.explore.content

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.ui.base.components.home.KatanaHomeScaffold
import dev.alvr.katana.ui.explore.component.ExploreComponent
import dev.alvr.katana.ui.explore.strings.LocalExploreStrings

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun ExploreContent(
    component: ExploreComponent,
    modifier: Modifier = Modifier,
) {
    val strings = LocalExploreStrings.current

    KatanaHomeScaffold(
        modifier = modifier,
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

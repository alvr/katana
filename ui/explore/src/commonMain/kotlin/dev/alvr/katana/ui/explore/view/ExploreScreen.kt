package dev.alvr.katana.ui.explore.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.ui.base.components.home.KatanaHomeScaffold
import dev.alvr.katana.ui.base.navigation.Destination
import katana.ui.explore.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
@Destination
@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalResourceApi::class,
    ExperimentalAnimationApi::class,
)
internal fun ExploreScreen() {
    KatanaHomeScaffold(
        title = stringResource(Res.string.explore_toolbar_title),
        searchPlaceholder = stringResource(Res.string.explore_toolbar_search_placeholder),
        onSearch = {},
        backContent = { Filter() },
    ) { paddingValues ->
        Text(
            modifier = Modifier.padding(paddingValues),
            text = stringResource(Res.string.explore_toolbar_title),
        )
    }
}

@Composable
private fun Filter() {
    Text(text = "Explore Filter")
}

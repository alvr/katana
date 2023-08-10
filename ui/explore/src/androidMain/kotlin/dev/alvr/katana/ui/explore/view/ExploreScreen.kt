package dev.alvr.katana.ui.explore.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import dev.alvr.katana.ui.base.components.home.KatanaHomeScaffold
import dev.alvr.katana.ui.explore.KR

@Composable
@Destination
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
internal fun ExploreScreen() {
    KatanaHomeScaffold(
        title = KR.string.explore_toolbar_title,
        searchPlaceholder = KR.string.explore_toolbar_search_placeholder,
        onSearch = {},
        backContent = { Filter() },
    ) { paddingValues ->
        Text(
            modifier = Modifier.padding(paddingValues),
            text = KR.string.explore_toolbar_title,
        )
    }
}

@Composable
private fun Filter() {
    Text(text = "Explore Filter")
}

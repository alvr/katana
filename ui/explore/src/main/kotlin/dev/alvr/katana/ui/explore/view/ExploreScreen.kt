package dev.alvr.katana.ui.explore.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import dev.alvr.katana.common.core.empty
import dev.alvr.katana.ui.base.components.home.KatanaHomeScaffold
import dev.alvr.katana.ui.explore.R

@Composable
@Destination
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
internal fun ExploreScreen() {
    KatanaHomeScaffold(
        title = R.string.explore_toolbar_title,
        search = String.empty,
        onSearch = {},
        backContent = { Filter() },
    ) { paddingValues ->
        Text(
            modifier = Modifier.padding(paddingValues),
            text = stringResource(R.string.explore_toolbar_title),
        )
    }
}

@Composable
private fun Filter() {
    Text(text = "Explore Filter")
}

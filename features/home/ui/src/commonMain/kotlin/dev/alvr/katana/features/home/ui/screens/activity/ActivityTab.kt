package dev.alvr.katana.features.home.ui.screens.activity

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.home.ui.screens.HomeTab
import dev.alvr.katana.features.home.ui.viewmodel.HomeIntent
import dev.alvr.katana.features.home.ui.viewmodel.HomeState

@Composable
@Suppress("UNUSED_PARAMETER")
internal fun ActivityTabContent(
    sessionActive: Boolean,
    uiState: HomeState.ActivityTabState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(HomeTab.Activity.title.value)
}

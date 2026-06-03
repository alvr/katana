package dev.alvr.katana.features.home.ui.screens.activity.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.home.ui.navigation.HomeNavigator
import dev.alvr.katana.features.home.ui.screens.HomeTab
import dev.alvr.katana.features.home.ui.screens.activity.viewmodel.ActivityViewModel
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
@Suppress("UNUSED_PARAMETER")
internal fun ActivityTabContent(
    navigator: HomeNavigator,
    sessionActive: Boolean,
    modifier: Modifier = Modifier,
    viewModel: ActivityViewModel = metroViewModel(),
) {
    Text(HomeTab.Activity.title.value)
}

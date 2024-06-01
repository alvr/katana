package dev.alvr.katana.features.social.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.alvr.katana.core.ui.components.home.KatanaHomeScaffold
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.screens.KatanaScreen
import dev.alvr.katana.features.social.ui.navigation.SocialNavigator
import dev.alvr.katana.features.social.ui.resources.Res
import dev.alvr.katana.features.social.ui.resources.social_toolbar_search_placeholder
import dev.alvr.katana.features.social.ui.resources.social_toolbar_title
import dev.alvr.katana.features.social.ui.viewmodel.SocialViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

fun NavGraphBuilder.social(socialNavigator: SocialNavigator) {
    composable(KatanaScreen.Social.name) {
        SocialScreen(socialNavigator)
    }
}

@Composable
@OptIn(
    KoinExperimentalAPI::class,
    ExperimentalMaterialApi::class,
)
@Suppress("UNUSED_PARAMETER")
private fun SocialScreen(
    navigator: SocialNavigator,
    viewModel: SocialViewModel = koinViewModel(),
) {
    KatanaHomeScaffold(
        title = Res.string.social_toolbar_title.value,
        searchPlaceholder = Res.string.social_toolbar_search_placeholder.value,
        onSearch = {},
        backContent = { Filter() },
    ) { paddingValues ->
        Text(
            modifier = Modifier.padding(paddingValues),
            text = Res.string.social_toolbar_title.value,
        )
    }
}

@Composable
private fun Filter() {
    Text(text = "Social Filter")
}

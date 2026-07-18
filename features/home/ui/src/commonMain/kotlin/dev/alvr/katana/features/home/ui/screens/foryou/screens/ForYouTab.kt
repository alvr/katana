package dev.alvr.katana.features.home.ui.screens.foryou.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.theme.KatanaTheme
import dev.alvr.katana.core.ui.theme.contentPaddingMedium
import dev.alvr.katana.core.ui.viewmodel.CollectEffect
import dev.alvr.katana.core.ui.viewmodel.collectUiStateWithLifecycle
import dev.alvr.katana.features.home.ui.navigation.HomeNavigator
import dev.alvr.katana.features.home.ui.screens.foryou.sections.Popular
import dev.alvr.katana.features.home.ui.screens.foryou.sections.Reading
import dev.alvr.katana.features.home.ui.screens.foryou.sections.Trending
import dev.alvr.katana.features.home.ui.screens.foryou.sections.Upcoming
import dev.alvr.katana.features.home.ui.screens.foryou.sections.Watching
import dev.alvr.katana.features.home.ui.screens.foryou.sections.WelcomeCard
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouEffect
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouIntent
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouState
import dev.alvr.katana.features.home.ui.screens.foryou.viewmodel.ForYouViewModel
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
internal fun ForYouTabContent(
    navigator: HomeNavigator,
    sessionActive: Boolean,
    modifier: Modifier = Modifier,
    viewModel: ForYouViewModel = metroViewModel(),
) {
    val uiState by viewModel.collectUiStateWithLifecycle()

    LaunchedEffect(sessionActive) { viewModel.intent(ForYouIntent.SessionChanged(sessionActive)) }

    viewModel.CollectEffect { effect ->
        when (effect) {
            ForYouEffect.NavigateToAnimeLists -> navigator.navigateToAnimeLists()
            ForYouEffect.NavigateToMangaLists -> navigator.navigateToMangaLists()
            ForYouEffect.NavigateToTrending -> navigator.navigateToTrending()
            ForYouEffect.NavigateToPopular -> navigator.navigateToPopular()
            ForYouEffect.NavigateToUpcoming -> navigator.navigateToUpcoming()
        }
    }

    ForYouTab(
        uiState = uiState,
        sessionActive = sessionActive,
        onIntent = viewModel::intent,
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
private fun ForYouTab(
    uiState: ForYouState,
    sessionActive: Boolean,
    onIntent: (ForYouIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.verticalScroll(scrollState).padding(WindowInsets.contentPaddingMedium.asPaddingValues()),
        verticalArrangement = Arrangement.spacedBy(KatanaTheme.dimensions.itemSpacing),
    ) {
        if (!sessionActive && uiState.showWelcomeCard) {
            WelcomeCard(onIntent = onIntent)
        }

        if (sessionActive) {
            Watching(status = uiState.watching, onIntent = onIntent)
            Reading(status = uiState.reading, onIntent = onIntent)
        }

        Trending(state = uiState.trending, onIntent = onIntent)
        Popular(state = uiState.popular, onIntent = onIntent)
        Upcoming(status = uiState.upcoming, onIntent = onIntent)
    }
}
